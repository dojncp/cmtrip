<template>
  <el-header class="header two-line-header">
    <div class="header-top">
      <i class="el-icon-arrow-left"></i>
      <span @click="goBackToHomepage" style="cursor: pointer; color: #ccc;">← Back Home</span>
    </div>
  </el-header>
  <el-container class="pass-entity-management">
    <el-aside width="300px" class="pass-list-container">
      <div class="pass-list-header">
        <h2 style="font-weight: bold;">My Passes</h2>
        <el-input
            v-model="passSearchQuery"
            placeholder="Search passes"
            clearable
            style="margin-bottom: 15px;"
            @clear="passSearchQuery = ''"
        >
          <template #prefix>
            <el-icon><search /></el-icon>
          </template>
        </el-input>
      </div>
      <el-scrollbar class="pass-scrollbar">
        <div
            v-for="pass in paginatedPasses"
            :key="pass.id"
            class="pass-item"
            :class="{ 'active-pass': activePassId === pass.id }"
            @click="selectPass(pass)"
        >
          <div class="pass-name">{{ pass.passName }}</div>
          <div class="pass-valid-days">
            <el-icon><calendar /></el-icon>
              Valid for: {{ pass.validDays }} days
          </div>
          <div class="pass-issue-company">
            <el-icon><location /></el-icon>
            {{ pass.issueCompany }}
          </div>
          <div class="pass-fare">
            <el-icon><location /></el-icon>
            {{ pass.fareCurrency }} {{ pass.fare }}
          </div>
        </div>
      </el-scrollbar>
      <el-pagination
          class="pass-pagination"
          small
          background
          layout="sizes, prev, pager, next"
          :page-sizes="[5, 10, 20, 50]"
          :page-size="passPageSize"
          :total="passTotal"
          :current-page="passCurrentPage"
          @size-change="handlePassSizeChange"
          @current-change="handlePassPageChange"
      />
    </el-aside>
    <el-main class="pass-entity-main-container">
      <div v-if="activePassId">
        <div class="pass-entity-header">
          <h2 style="font-weight: bold;">Entities for: {{ activePass?.passName }}</h2>
          <div style="display: flex; align-items: center;">
            <el-input
                v-model="searchQuery"
                placeholder="Search by entity name"
                style="width: 240px; margin-right: 20px;"
                clearable
                @clear="searchQuery = ''"
                @keyup.enter="handleSearch"
            >
              <template #prefix>
                <el-icon><search /></el-icon>
              </template>
            </el-input>
            <el-button type="primary" @click="openAddPassEntityDialog">+ New Entity</el-button>
          </div>
        </div>
        <div v-if="paginatedPassEntities && paginatedPassEntities.length > 0">
          <el-table :data="paginatedPassEntities" style="width: 100%" v-loading="loading">
            <el-table-column type="index" width="50"></el-table-column>
            <el-table-column prop="entityName" label="Entity Name"></el-table-column>
            <el-table-column label="Valid Period">
              <template #default="{ row }">
                <div>{{ formatDate(row.passStartDate) }}</div>
                <div>to {{ formatDate(row.passEndDate) }}</div>
              </template>
            </el-table-column>
            <el-table-column label="Description">
              <template #default="{ row }">
                <el-button v-if="row.description" size="small" @click="showDescription(row)" style="height: auto">
                  View <br/> Description
                </el-button>
                <span v-else> </span>
              </template>
            </el-table-column>
            <el-table-column>
              <template #header>
                <el-tooltip
                    content="Calculated as the price of the pass minus the total cost of all trips using the pass."
                    placement="top"
                >
                  <span style="cursor: help;">Saved Fare</span>
                </el-tooltip>
              </template>
              <template #default="{ row }">
                <div v-if="savedFares[row.id] !== undefined">
                  {{ savedFares[row.id] !== null ? `${activePass?.fareCurrency} ${savedFares[row.id]}` : '' }}
                </div>
                <div v-else>
                  Loading...
                </div>
              </template>
            </el-table-column>
            <el-table-column label="Operations">
              <template #default="{ row }">
                <el-button size="small" @click="openEditPassEntityDialog(row)">Edit</el-button>
                <el-button size="small" type="danger" @click="deletePassEntity(row)">Delete</el-button>
              </template>
            </el-table-column>
            <el-table-column label="Remark">
              <template #default="{ row }">
                <el-button v-if="row.remark" size="small" @click="showRemark(row)" style="height: auto">View <br/> Remark</el-button>
                <span v-else> </span>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination
              class="pagination"
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
              :current-page="currentPage"
              :page-sizes="[5, 10, 20, 50]"
              :page-size="pageSize"
              layout="total, sizes, prev, pager, next, jumper"
              :total="filteredPassEntities.length"
          />
        </div>
        <div v-else class="empty-state">
          <el-empty description="No certain data" />
        </div>
      </div>
      <div v-else class="empty-state">
        <el-empty description="Please select a pass from the left panel to view entities." />
      </div>
      <el-dialog
          v-model="passEntityDialogVisible"
          :title="isEditingPassEntity ? 'Edit Entity' : 'New Entity'"
          width="500px"
      >
        <el-form
            ref="passEntityFormRef"
            :rules="passEntityRules"
            :model="currentPassEntityForm"
            label-width="120px"
        >
          <el-form-item label="Name" prop="entityName">
            <el-input v-model="currentPassEntityForm.entityName" />
          </el-form-item>
          <el-form-item label="Start Date" prop="passStartDate">
            <el-date-picker
                v-model="currentPassEntityForm.passStartDate"
                type="date"
                placeholder="Pick the date"
                style="width: 300px"
            />
          </el-form-item>
          <el-form-item label="End Date" prop="passEndDate">
            <el-date-picker
                disabled
                v-model="currentPassEntityForm.passEndDate"
                type="date"
                placeholder="Being Calculated automatically"
                style="width: 300px"
            />
          </el-form-item>
          <el-form-item label="Description" prop="description">
            <el-input
                v-model="currentPassEntityForm.description"
                type="textarea"
                rows="3"
                placeholder="Enter description if any"
            />
          </el-form-item>
          <el-form-item label="Remark">
            <el-input
                v-model="currentPassEntityForm.remark"
                type="textarea"
                rows="3"
                placeholder="Enter remarks if any"
            />
          </el-form-item>
        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="passEntityDialogVisible = false">Cancel</el-button>
            <el-button type="primary" @click="submitPassEntityForm">Submit</el-button>
          </span>
        </template>
      </el-dialog>
      <el-dialog v-model="descriptionDialogVisible" :title=" 'Description of '+ currentPassEntityName" width="500px">
        <div style="white-space: pre-line;">{{ currentDescription }}</div>
        <template #footer>
          <el-button type="primary" @click="descriptionDialogVisible = false">Close</el-button>
        </template>
      </el-dialog>

      <!-- Remark popup 备注弹窗 -->
      <el-dialog v-model="remarkDialogVisible" :title="'Remark of ' + currentPassEntityName" width="500px">
        <div style="white-space: pre-line;">{{ currentRemark }}</div>
        <template #footer>
          <el-button type="primary" @click="remarkDialogVisible = false">Close</el-button>
        </template>
      </el-dialog>

    </el-main>
  </el-container>
</template>

<script setup>
import {ref, computed, onMounted, watch} from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { Search, Calendar, Location } from '@element-plus/icons-vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { getMyTrips } from '@/api/trip';
import {
  getMyActionsByTripId,
  addAction,
  addActionWithImage,
  editAction,
  editActionWithImage,
  deleteActionById, getPassEntitySavedFare
} from '@/api/action';
import {
  getFullImageUrl,
  formatDate,
  formatDateTime,
  formatDateTimeWithoutTimezone, formatDateWithoutTimezone
} from '@/utils/utils';
import {getAllPasses} from "@/api/pass";
import {addPassEntity, deletePassEntityById, editPassEntity, getMyPassEntitiesByPassId} from "@/api/passEntity";

// Description related 描述相关
const descriptionDialogVisible = ref(false);
const currentDescription = ref('');
const currentPassEntityName = ref('');
// Show description 显示描述
const showDescription = (row) => {
  currentDescription.value = row.description;
  currentPassEntityName.value = row.entityName;
  descriptionDialogVisible.value = true;
}

// Use the object to store the entity id and the fares
// 使用对象来存储，key 是 entity id，value 是 saved fare
const savedFares = ref({});
// Function of fetching saved fares 获取省钱方法
const fetchSavedFare = async (entityId) => {
  try {
    const response = await getPassEntitySavedFare(entityId);
    // Update the saveFares 更新 savedFares，确保响应式更新
    savedFares.value = {
      ...savedFares.value,
      [entityId]: response.data.data
    };
  } catch (error) {
    console.error('Failed to fetch saved fare:', error);
    savedFares.value = {
      ...savedFares.value,
      [entityId]: null
    };
  }
};

const route = useRoute();
const router = useRouter();

const goBackToHomepage = () => {
  router.push("/home");
};

// Pass-related 通票相关
const passes = ref([]);
const activePassId = ref(null);
const activePass = ref(null);
const currentPassValidDays = ref(0);
const passSearchQuery = ref('');
// Pass pagination 通票分页
const passCurrentPage = ref(1);
const passPageSize = ref(5);
const passTotal = computed(() => filteredPasses.value.length);
const paginatedPasses = computed(() => {
  const start = (passCurrentPage.value - 1) * passPageSize.value;
  const end = start + passPageSize.value;
  return filteredPasses.value.slice(start, end);
});
const handlePassSizeChange = (val) => {
  passPageSize.value = val;
  passCurrentPage.value = 1;
};
const handlePassPageChange = (val) => {
  passCurrentPage.value = val;
};

const passEntities = ref([]);
const loading = ref(false);

// Pagination-related 分页相关
const currentPage = ref(1);
const pageSize = ref(10);
const handleSizeChange = (val) => {
  pageSize.value = val;
  currentPage.value = 1;
};
const handleCurrentChange = (val) => {
  currentPage.value = val;
};

// Filtering and pagination computed properties 过滤和分页计算属性
const filteredPasses = computed(() => {
  if (!passSearchQuery.value) return passes.value;
  return passes.value.filter(pass =>
      pass.passName.toLowerCase().includes(passSearchQuery.value.toLowerCase()) ||
      pass.validDays.toLowerCase().includes(passSearchQuery.value.toLowerCase()) ||
      pass.issueCompany.toLowerCase().includes(passSearchQuery.value.toLowerCase()) ||
      pass.fare.toLowerCase().includes(passSearchQuery.value.toLowerCase()) ||
      pass.fareCompany.toLowerCase().includes(passSearchQuery.value.toLowerCase())
  );
});
// Pass entity computation with search functionality included 包含搜索功能的entity计算
const filteredPassEntities = computed(() => {
  if (!searchQuery.value) {
    return passEntities.value;
  }
  return passEntities.value.filter(passEntity =>
    passEntity.entityName.toLowerCase().includes(searchQuery.value.toLowerCase())
  );
});

const paginatedPassEntities = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value;
  const end = start + pageSize.value;
  return filteredPassEntities.value.slice(start, end);
});

// Search query 搜索查询
const searchQuery = ref('');
// Handle search 处理搜索
const handleSearch = () => {
  currentPage.value = 1; // 搜索时重置到第一页
};

// Remark-related 备注相关
const remarkDialogVisible = ref(false);
const currentRemark = ref('');

// Display the remark 显示备注
const showRemark = (row) => {
  currentRemark.value = row.remark;
  currentPassEntityName.value = row.entityName;
  remarkDialogVisible.value = true;
};

// Get the list of passes 获取通票列表
const fetchPasses = async () => {
  try {
    const response = await getAllPasses();
    passes.value = response.data.data;
    // If the route contains a passId parameter, directly select the corresponding pass 如果路由中有passId参数，则直接选中对应的通票
    if (route.query.passId) {
      const pass = passes.value.find(t => t.id === parseInt(route.query.passId));
      if (pass) {
        await selectPass(pass);
      }
    }
  } catch (error) {
    ElMessage.error('Failed to fetch passes');
    console.error(error);
  }
};

// Select the pass 选择通票
const selectPass = async (pass) => {
  activePassId.value = pass.id;
  currentPassValidDays.value = pass.validDays;
  activePass.value = pass;
  await fetchPassEntities(pass.id);
  // Update the URL without triggering navigation 更新URL但不触发导航
  await router.replace({
    query: {...route.query, passId: pass.id}
  });
};

// Get the pass entity list 获取实体列表
const fetchPassEntities = async (passId) => {
  loading.value = true;
  try {
    const response = await getMyPassEntitiesByPassId(passId);
    passEntities.value = response.data.data;
    currentPage.value = 1;
    // Retrieve saved fares for every entity 为每个通票实体获取其省钱金额
    for (const entity of passEntities.value) {
      await fetchSavedFare(entity.id);
    }
  } catch (error) {
    ElMessage.error('Failed to fetch entities');
    console.error(error);
  } finally {
    loading.value = false;
  }
};

// Pass Entity form related 实体表单相关
const passEntityDialogVisible = ref(false);
const isEditingPassEntity = ref(false);
const currentPassEntityForm = ref({
  id: null,
  passId: '',
  entityName: '',
  passStartDate: null,
  passEndDate: null,
  description: '',
  remark: ''
});
const passEntityFormRef = ref();
const passEntityRules = {
  entityName: [{ required: true, message: 'Please input the entity name', trigger: 'blur' }],
  passStartDate: [{ required: true, message: 'Please select the start date', trigger: 'change' }],
  passEndDate: [{ required: true, message: 'Please select the end date', trigger: 'change' }],
};

// Open the "Add Pass Entity" dialog 打开新增实体对话框
const openAddPassEntityDialog = () => {
  isEditingPassEntity.value = false;
  currentPassEntityForm.value = {
    id: null,
    passId: '',
    entityName: '',
    passStartDate: null,
    passEndDate: null,
    description: '',
    remark: ''
  };
  passEntityDialogVisible.value = true;
};

// Open the "Edit Pass Entity" dialog 打开编辑实体对话框
const openEditPassEntityDialog = (passEntity) => {
  isEditingPassEntity.value = true;
  currentPassEntityForm.value = { ...passEntity };
  passEntityDialogVisible.value = true;
};

// Watch the change of passStartDate 引入watch以监视passStartDate的变化
watch(() => currentPassEntityForm.value.passStartDate, (newStartDate) => {
  if (newStartDate && currentPassValidDays.value > 0) {
    // 计算 end date
    const endDate = new Date(newStartDate);
    endDate.setDate(endDate.getDate() + currentPassValidDays.value - 1);
    currentPassEntityForm.value.passEndDate = endDate;
  } else {
    currentPassEntityForm.value.passEndDate = null;
  }
});

// Submit the pass entity form 提交实体表单
const submitPassEntityForm = async () => {
  passEntityFormRef.value.validate(async (valid) => {
    if (!valid) {
      ElMessage.error('Please fill in the required fields.')
      return
    }
    // Submit 提交
    try {
      console.log("currentPassEntityForm: ", currentPassEntityForm.value)
      // Fix the time 固化时间
      const rightTimePassEntityForm = {
        ...currentPassEntityForm.value,
        passStartDate: formatDateWithoutTimezone(currentPassEntityForm.value.passStartDate),
        passEndDate: formatDateWithoutTimezone(currentPassEntityForm.value.passEndDate),
      };
      if (isEditingPassEntity.value) {
        await editPassEntity(rightTimePassEntityForm)
        ElMessage.success('Entity updated successfully');
      } else {
        console.log("activePassId: ", activePassId.value)
        console.log("rightTimePassEntityForm.value: ", rightTimePassEntityForm.value)
        rightTimePassEntityForm.passId = activePassId.value;
        console.log("rightTimePassEntityForm.value-new: ", rightTimePassEntityForm.value)
        await addPassEntity(rightTimePassEntityForm);
        ElMessage.success('Entity created successfully');
      }
      passEntityDialogVisible.value = false;
      await fetchPassEntities(activePassId.value);
    } catch (error) {
      if (error !== 'cancel') {
        console.error(error);
      }
    }
  });
};

// Delete the pass entity 删除实体
const deletePassEntity = async (passEntity) => {
  try {
    await ElMessageBox.confirm(
        `Are you sure to delete entity "${passEntity.entityName}"?`,
        'Warning',
        { confirmButtonText: 'OK', cancelButtonText: 'Cancel', type: 'warning' }
    );
    await deletePassEntityById(passEntity.id);
    ElMessage.success('Entity deleted successfully');
    await fetchPassEntities(activePassId.value);
  } catch (error) {
    if (error !== 'cancel') {
      console.error(error);
    }
  }
};

onMounted(() => {
  fetchPasses();
});

</script>

<style scoped>

.header {
  height: auto !important;
  padding: 10px 20px;
}

.two-line-header {
  display: flex;
  flex-direction: column;
  min-height: auto;
}

.header-top {
  display: flex;
  align-items: center;
  margin-bottom: 15px;
}

.header-top .el-icon-arrow-left {
  margin-right: 8px;
}

.pass-entity-management {
  flex: 1;
  display: flex;
  height: auto;
  min-height: 600px;
}

.pass-list-container {
  flex: 0 0 300px;
  border-right: 1px solid #e6e6e6;
  background-color: #f5f7fa;
  padding: 20px;
  display: flex;
  flex-direction: column;
  min-height: 0px;
}

.pass-list-header {
  margin-bottom: 15px;
}

.pass-scrollbar {
  flex: 1;
  min-height: 0px;
  overflow-y: auto;
}

.pass-item {
  padding: 15px;
  margin-bottom: 10px;
  background-color: white;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s;
  border: 1px solid #ebeef5;
}

.pass-item:hover {
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.pass-item.active-pass {
  border-left: 4px solid #564a53;
  background-color: rgba(239, 225, 236, 0.23);
}

.pass-name {
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 8px;
  color: #303133;
}

.pass-valid-days, .pass-issue-company, .pass-fare {
  font-size: 13px;
  color: #606266;
  margin-bottom: 5px;
  display: flex;
  align-items: center;
}

.active-pass, .pass-issue-company .pass-fare {
  margin-right: 5px;
}

.pass-entity-main-container {
  flex: 1;
  overflow-y: auto;
  min-height: 0;
  padding: 20px;
  background-color: white;
}

.pass-entity-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.empty-state {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 70vh;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.pass-pagination {
  margin-top: 10px;
  text-align: center;
}

</style>