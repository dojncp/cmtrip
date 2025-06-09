<template>
  <el-header class="header two-line-header">
    <div class="header-top">
      <i class="el-icon-arrow-left"></i>
      <span @click="goBackToHomepage" style="cursor: pointer; color: #ccc;">← Back Home</span>
    </div>
  </el-header>
  <el-container class="action-management">
    <el-aside width="300px" class="trip-list-container">
      <div class="trip-list-header">
        <h2 style="font-weight: bold;">My Trips</h2>
        <el-input
            v-model="tripSearchQuery"
            placeholder="Search trips"
            clearable
            style="margin-bottom: 15px;"
            @clear="tripSearchQuery = ''"
        >
          <template #prefix>
            <el-icon><search /></el-icon>
          </template>
        </el-input>
      </div>
      <el-scrollbar class="trip-scrollbar">
        <div
            v-for="trip in paginatedTrips"
            :key="trip.id"
            class="trip-item"
            :class="{ 'active-trip': activeTripId === trip.id }"
            @click="selectTrip(trip)"
        >
          <div class="trip-name">{{ trip.tripName }}</div>
          <div class="trip-dates">
            <el-icon><calendar /></el-icon>
            {{ formatDate(trip.tripStartDate) }} - {{ formatDate(trip.tripEndDate) }}
          </div>
          <div class="trip-locations">
            <el-icon><location /></el-icon>
            {{ trip.tripFrom }} → {{ trip.tripTo }}
          </div>
        </div>
      </el-scrollbar>
      <el-pagination
          class="trip-pagination"
          small
          background
          layout="sizes, prev, pager, next"
          :page-sizes="[5, 10, 20, 50]"
          :page-size="tripPageSize"
          :total="tripTotal"
          :current-page="tripCurrentPage"
          @size-change="handleTripSizeChange"
          @current-change="handleTripPageChange"
      />
    </el-aside>
    <el-main class="action-main-container">
      <div v-if="activeTripId">
        <div class="action-header">
          <h2 style="font-weight: bold;">Actions for: {{ activeTrip?.tripName }}</h2>
          <div style="display: flex; align-items: center;">
            <el-input
                v-model="searchQuery"
                placeholder="Search by action name"
                style="width: 240px; margin-right: 20px;"
                clearable
                @clear="searchQuery = ''"
                @keyup.enter="handleSearch"
            >
              <template #prefix>
                <el-icon><search /></el-icon>
              </template>
            </el-input>
            <el-button type="primary" @click="openAddActionDialog">+ New Action</el-button>
          </div>
        </div>
        <div v-if="paginatedActions && paginatedActions.length > 0">
          <el-table :data="paginatedActions" style="width: 100%" v-loading="loading">
            <el-table-column type="index" width="50"></el-table-column>
            <el-table-column prop="actionName" label="Action Name" width="150"></el-table-column>
            <el-table-column prop="actionType" label="Type" width="100"></el-table-column>
            <el-table-column label="Time" width="180">
              <template #default="{ row }">
                <div>{{ formatDateTime(row.actionStartTime) }}</div>
                <div>to {{ formatDateTime(row.actionEndTime) }}</div>
              </template>
            </el-table-column>
            <el-table-column prop="fare" label="Fare"></el-table-column>
            <el-table-column prop="fareCurrency" label="Currency"></el-table-column>
            <el-table-column label="Description"  width="160">
              <template #default="{ row }">
                <el-button v-if="row.description" size="small" @click="showDescription(row)" style="height: auto">
                  View <br/> Description
                </el-button>
                <span v-else> </span>
              </template>
            </el-table-column>
            <el-table-column label="Image"  width="160">
              <template #default="{ row }">
                <el-image
                    v-if="row.imgPath"
                    :src="getFullImageUrl(row.imgPath)"
                    style="width: 80px; height: 80px;"
                    fit="cover"
                    :preview-src-list="[getFullImageUrl(row.imgPath)]"
                    :append-to-body="true"
                    :preview-teleported="true"
                />
              </template>
            </el-table-column>
            <el-table-column label="Operations" width="150">
              <template #default="{ row }">
                <el-button size="small" @click="openEditActionDialog(row)">Edit</el-button>
                <el-button size="small" type="danger" @click="deleteAction(row)">Delete</el-button>
              </template>
            </el-table-column>
            <el-table-column label="Pass" width="120">
              <template #default="{ row }">
                <el-button size="small" type="info" @click="openPassOperationDialog(row)">Check</el-button>
              </template>
            </el-table-column>
            <el-table-column label="Remark"  width="120">
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
              :total="filteredActions.length"
          />
        </div>
        <div v-else class="empty-state">
          <el-empty description="No certain data" />
        </div>
      </div>
      <div v-else class="empty-state">
        <el-empty description="Please select a trip from the left panel to view actions." />
      </div>
      <el-dialog
          v-model="actionDialogVisible"
          :title="isEditingAction ? 'Edit Action' : 'New Action'"
          width="500px"
      >
        <el-form
            ref="actionFormRef"
            :rules="actionRules"
            :model="currentActionForm"
            label-width="120px"
        >
          <el-form-item label="Name" prop="actionName">
            <el-input v-model="currentActionForm.actionName" />
          </el-form-item>
          <el-form-item label="Type" prop="actionType">
            <el-select v-model="currentActionForm.actionType" placeholder="Select">
              <el-option label="Transportation" value="Transportation" />
              <el-option label="Sightseeing" value="Sightseeing" />
              <el-option label="Rest" value="Rest" />
              <el-option label="Accommodation" value="Accommodation" />
              <el-option label="Meal" value="Meal" />
              <el-option label="Other" value="Other" />
            </el-select>
          </el-form-item>
          <el-form-item label="Start Time" prop="actionStartTime">
            <el-date-picker
                v-model="currentActionForm.actionStartTime"
                type="datetime"
                placeholder="Pick the date and the time"
            />
          </el-form-item>
          <el-form-item label="End Time" prop="actionEndTime">
            <el-date-picker
                v-model="currentActionForm.actionEndTime"
                type="datetime"
                placeholder="Pick the date and the time"
            />
          </el-form-item>
          <el-form-item label="Fare" prop="fare">
            <el-input-number
                v-model="currentActionForm.fare"
                :min="0"
                :step="0.1"
                controls-position="right"
            />
          </el-form-item>
          <el-form-item label="Fare Currency" prop="fareCurrency">
            <el-select v-model="currentActionForm.fareCurrency" placeholder="Select">
              <el-option label="USD" value="USD" />
              <el-option label="CNY" value="CNY" />
              <el-option label="EUR" value="EUR" />
              <el-option label="JPY" value="JPY" />
              <el-option label="HKD" value="HKD" />
              <el-option label="GBP" value="GBP" />
              <el-option label="CAD" value="CAD" />
              <el-option label="AUD" value="AUD" />
            </el-select>
          </el-form-item>
          <el-form-item label="Description" prop="description">
            <el-input v-model="currentActionForm.description" />
          </el-form-item>
          <el-form-item label="Remark">
            <el-input
                v-model="currentActionForm.remark"
                type="textarea"
                rows="3"
                placeholder="Enter remarks if any"
            />
          </el-form-item>
          <el-form-item label="Image Upload">
            <el-upload
                action=""
                :auto-upload="false"
                :on-change="handleActionImageChange"
                :show-file-list="false"
            >
              <el-button type="primary">Upload Image</el-button>
            </el-upload>
          </el-form-item>
          <el-form-item>
            <div class="el-upload__tip">Image files with a size less than 10MB</div>
          </el-form-item>
          <el-form-item>
            <el-image
                v-if="actionPreviewUrl"
                :src="actionPreviewUrl"
                fit="contain"
            />
            <el-image
                v-if="isEditingAction && currentActionForm.imgPath && !actionSelectedFile"
                :src="getFullImageUrl(currentActionForm.imgPath)"
                style="max-width: 200px; max-height: 200px;"
                fit="contain"
            />
          </el-form-item>
          <el-form-item>
            <div slot="tip" class="el-upload__tip" style="color: red">
              Just click the button "Upload Image" to upload a new image.
            </div>
          </el-form-item>
        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="actionDialogVisible = false">Cancel</el-button>
            <el-button type="primary" @click="submitActionForm">Submit</el-button>
          </span>
        </template>
      </el-dialog>
      <el-dialog v-model="descriptionDialogVisible" :title=" 'Description of '+ currentActionName" width="500px">
        <div style="white-space: pre-line;">{{ currentDescription }}</div>
        <template #footer>
          <el-button type="primary" @click="descriptionDialogVisible = false">Close</el-button>
        </template>
      </el-dialog>

      <!-- Remark popup 备注弹窗 -->
      <el-dialog v-model="remarkDialogVisible" :title="'Remark of ' + currentActionName" width="500px">
        <div style="white-space: pre-line;">{{ currentRemark }}</div>
        <template #footer>
          <el-button type="primary" @click="remarkDialogVisible = false">Close</el-button>
        </template>
      </el-dialog>

      <!-- Pass entity operation Dialog 通票实体操作对话框 -->
      <el-dialog
          v-model="passOperationDialog.visible"
          :title="passOperationDialog.title"
          width="35%"
      >
        <el-scrollbar v-loading="passOperationDialog.loading">
          <el-table
              :data="passOperationDialog.allPassEntities"
              style="width: 100%"
              @row-click="(row) => passOperationDialog.selectedPassEntity = row"
          >
            <el-table-column type="index" width="50"></el-table-column>
            <el-table-column width="150" align="center" label="Entity Name">
              <template #default="{ row }">
                <el-radio
                    v-model="passOperationDialog.selectedPassEntityId"
                    :label="row.id"
                    @change="(val) => handlePassEntityChange(val, row)"
                    @click.stop
                >
                {{ row.entityName }}
                </el-radio>
              </template>
            </el-table-column>
            <el-table-column label="Valid Period" width="140">
              <template #default="{ row }">
                <div>{{ formatDate(row.passStartDate) }}</div>
                <div>to {{ formatDate(row.passEndDate) }}</div>
              </template>
            </el-table-column>
            <el-table-column label="Description">
              <template #default="{ row }">
                <el-button
                    v-if="row.description"
                    size="small"
                    @click.stop="showPassEntityDescription(row)"
                    style="height: auto"
                >
                  View
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-scrollbar>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="passOperationDialog.visible = false">Cancel</el-button>
            <el-button
                type="primary"
                @click="savePassEntityBinding"
                :disabled="!passOperationDialog.selectedPassEntityId"
            >
              Bind
            </el-button>
          </span>
        </template>
      </el-dialog>
      <!-- Pass entity description dialog 通票实体描述对话框-->
      <el-dialog
          v-model="passEntityDescriptionDialog.visible"
          :title="'Description of ' + currentPassEntityName"
          width="500px"
      >
        <div style="white-space: pre-line;">{{ currentPassEntityDescription }}</div>
        <template #footer>
          <el-button type="primary" @click="passEntityDescriptionDialog.visible = false">Close</el-button>
        </template>
      </el-dialog>

    </el-main>
  </el-container>
</template>

<script setup>
import {ref, computed, onMounted, reactive} from 'vue';
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
  deleteActionById, bindActionToPassEntity, getBoundPassEntityOfTheAction
} from '@/api/action';
import {
  getFullImageUrl,
  formatDate,
  formatDateTime,
  formatDateTimeWithoutTimezone
} from '@/utils/utils';
import {getMyPassEntities} from "@/api/passEntity";

// Description related 描述相关
const descriptionDialogVisible = ref(false);
const currentDescription = ref('');
const currentActionName = ref('');
// Show description 显示描述
const showDescription = (row) => {
  currentDescription.value = row.description;
  currentActionName.value = row.actionName;
  descriptionDialogVisible.value = true;
}

// Update the selectedPassEntityName when the status of radio changes 当单选框选中状态变化时，更新 selectedPassEntityName
const handlePassEntityChange = (selectedId, row) => {
  if (selectedId) {
    // Find the selected entity to update the entity name 找到选中的实体，并更新名称
    const selectedEntity = passOperationDialog.allPassEntities.find(
        e => e.id === selectedId
    );
    if (selectedEntity) {
      passOperationDialog.selectedPassEntityName = selectedEntity.entityName;
    }
  } else {
    passOperationDialog.selectedPassEntityName = null;
  }
};

// Pass entity operation dialog 通票实体操作对话框
const passOperationDialog = reactive({
  visible: false,
  title: 'Bind Pass Entity',
  currentActionId: null,
  currentActionName: '',
  allPassEntities: [],
  selectedPassEntityName: '',
  selectedPassEntityId: null,
  loading: false
});
// Open the pass entity operation dialog 打开通票实体操作对话框
const openPassOperationDialog = async (action) => {
  try {
    passOperationDialog.loading = true;
    passOperationDialog.currentActionId = action.id;
    passOperationDialog.currentActionName = action.actionName;
    passOperationDialog.title = `Bind Pass Entity for ${passOperationDialog.currentActionName}`;
    // Get all pass entities 获取所有通票实体
    const response = await getMyPassEntities();
    passOperationDialog.allPassEntities = response.data.data || [];
    // Get bound pass entity 获取已绑定的通票实体
    const boundResponse = await getBoundPassEntityOfTheAction(action.id);
    const boundEntity = boundResponse.data.data;
    // If bound, set the selected status 如果有绑定，则设置选中状态
    passOperationDialog.selectedPassEntityId = boundEntity?.id || null;
    passOperationDialog.selectedPassEntityName = boundEntity?.entityName || '';
    passOperationDialog.visible = true;
  } catch (error) {
    console.error(error);
  } finally {
    passOperationDialog.loading = false;
  }
};
// Save the binding operation 保存绑定操作
const savePassEntityBinding = async () => {
  if (!passOperationDialog.selectedPassEntityId) {
    await ElMessageBox.confirm(
        'Cancel operation?',
        'Confirm Operation',
        {confirmButtonText: 'Confirm', cancelButtonText: 'Cancel', type: 'warning'}
    );
    passOperationDialog.visible = false;
    return;
  }
  try {
    await ElMessageBox.confirm(
        `Are you sure to bind the action "${passOperationDialog.currentActionName}" to pass entity "${passOperationDialog.selectedPassEntityName}"?`,
        'Confirm Binding',
        { confirmButtonText: 'Confirm', cancelButtonText: 'Cancel', type: 'warning' }
    );
    await bindActionToPassEntity(
        passOperationDialog.currentActionId,
        passOperationDialog.selectedPassEntityId
    );
    ElMessage.success('Pass entity bound successfully');
    passOperationDialog.visible = false;
  } catch (error) {
    if (error !== 'cancel') {
      console.error(error);
    }
  }
};
// Pass Entity Description related 通票实体描述相关
const passEntityDescriptionDialog = reactive({
  visible: false
});
const currentPassEntityDescription = ref('');
const currentPassEntityName = ref('');
// Show pass entity description 显示通票实体描述
const showPassEntityDescription = (row) => {
  currentPassEntityDescription.value = row.description;
  currentPassEntityName.value = row.entityName;
  passEntityDescriptionDialog.visible = true;
};

const route = useRoute();
const router = useRouter();
const goBackToHomepage = () => {
  router.push("/home");
};

// Trip-related data 行动相关数据
const trips = ref([]);
const activeTripId = ref(null);
const activeTrip = ref(null);
const tripSearchQuery = ref('');
// Trip pagination 行程分页
const tripCurrentPage = ref(1);
const tripPageSize = ref(5);
const tripTotal = computed(() => filteredTrips.value.length);
const paginatedTrips = computed(() => {
  const start = (tripCurrentPage.value - 1) * tripPageSize.value;
  const end = start + tripPageSize.value;
  return filteredTrips.value.slice(start, end);
});
const handleTripSizeChange = (val) => {
  tripPageSize.value = val;
  tripCurrentPage.value = 1;
};
const handleTripPageChange = (val) => {
  tripCurrentPage.value = val;
};

const actions = ref([]);
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
const filteredTrips = computed(() => {
  if (!tripSearchQuery.value) return trips.value;
  return trips.value.filter(trip =>
      trip.tripName.toLowerCase().includes(tripSearchQuery.value.toLowerCase()) ||
      trip.tripFrom.toLowerCase().includes(tripSearchQuery.value.toLowerCase()) ||
      trip.tripTo.toLowerCase().includes(tripSearchQuery.value.toLowerCase())
  );
});
// Action computation with search functionality included 包含搜索功能的action计算
const filteredActions = computed(() => {
  if (!searchQuery.value) {
    return actions.value;
  }
  return actions.value.filter(action =>
    action.actionName.toLowerCase().includes(searchQuery.value.toLowerCase())
  );
});

const paginatedActions = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value;
  const end = start + pageSize.value;
  return filteredActions.value.slice(start, end);
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
  currentActionName.value = row.actionName;
  remarkDialogVisible.value = true;
};

// Get the list of trips 获取行程列表
const fetchTrips = async () => {
  try {
    const response = await getMyTrips();
    trips.value = response.data.data;
    // If the route contains a tripId parameter, directly select the corresponding trip 如果路由中有tripId参数，则直接选中对应的行程
    if (route.query.tripId) {
      const trip = trips.value.find(t => t.id === parseInt(route.query.tripId));
      if (trip) {
        await selectTrip(trip);
      }
    }
  } catch (error) {
    ElMessage.error('Failed to fetch trips');
    console.error(error);
  }
};

// Select the trip 选择行程
const selectTrip = async (trip) => {
  activeTripId.value = trip.id;
  activeTrip.value = trip;
  await fetchActions(trip.id);
  // Update the URL without triggering navigation 更新URL但不触发导航
  await router.replace({
    query: {...route.query, tripId: trip.id}
  });
};

// Get the action list 获取行动列表
const fetchActions = async (tripId) => {
  loading.value = true;
  try {
    const response = await getMyActionsByTripId(tripId);
    actions.value = response.data.data;
    currentPage.value = 1;
  } catch (error) {
    ElMessage.error('Failed to fetch actions');
    console.error(error);
  } finally {
    loading.value = false;
  }
};

// Action form related 行动表单相关
const actionDialogVisible = ref(false);
const isEditingAction = ref(false);
const currentActionForm = ref({
  id: null,
  actionName: '',
  actionType: '',
  actionStartTime: null,
  actionEndTime: null,
  fare: null,
  fareCurrency: '',
  description: '',
  imgPath: '',
  remark: ''
});
const actionFormRef = ref();
const actionRules = {
  actionName: [{ required: true, message: 'Please input the action name', trigger: 'blur' }],
  actionType: [{ required: true, message: 'Please select the action type', trigger: 'change' }],
  actionStartTime: [{ required: true, message: 'Please select the start time', trigger: 'change' }],
  actionEndTime: [{ required: true, message: 'Please select the end time', trigger: 'change' }],
  fare: [{ required: true, message: 'Please input the fare', trigger: 'blur' }],
  fareCurrency: [{ required: true, message: 'Please select the fare Currency', trigger: 'blur' }]
};

// Image upload related 图片上传相关
const actionSelectedFile = ref(null);
const actionPreviewUrl = ref(null);
const handleActionImageChange = (file) => {
  const isImage = /\.(jpg|jpeg|png|gif|bmp)$/i.test(file.name);
  if (!isImage) {
    ElMessage.error('Only image files are allowed');
    return false;
  }
  if (file.size > 10 * 1024 * 1024) {
    ElMessage.error('Image size should not exceed 10MB');
    return false;
  }
  actionSelectedFile.value = file.raw;
  const reader = new FileReader();
  reader.onload = (e) => {
    actionPreviewUrl.value = e.target.result;
  };
  reader.readAsDataURL(actionSelectedFile.value);
};

// Open the "Add Action" dialog 打开新增行动对话框
const openAddActionDialog = () => {
  isEditingAction.value = false;
  currentActionForm.value = {
    id: null,
    actionName: '',
    actionType: '',
    actionStartTime: null,
    actionEndTime: null,
    fare: null,
    fareCurrency: '',
    description: '',
    imgPath: '',
    remark: ''
  };
  actionSelectedFile.value = null;
  actionPreviewUrl.value = null;
  actionDialogVisible.value = true;
};

// Open the "Edit Action" dialog 打开编辑行动对话框
const openEditActionDialog = (action) => {
  isEditingAction.value = true;
  currentActionForm.value = { ...action };
  actionSelectedFile.value = null;
  actionPreviewUrl.value = null;
  actionDialogVisible.value = true;
};

// Submit the action form 提交行动表单
const submitActionForm = async () => {
  actionFormRef.value.validate(async (valid) => {
    if (!valid) {
      ElMessage.error('Please fill in the required fields.')
      return
    }
    try {
      // Fix the time 固化时间
      const rightTimeActionForm = {
        ...currentActionForm.value,
        actionStartTime: formatDateTimeWithoutTimezone(currentActionForm.value.actionStartTime),
        actionEndTime: formatDateTimeWithoutTimezone(currentActionForm.value.actionEndTime),
      };
      if (isEditingAction.value) {
        if (actionSelectedFile.value) {
          await editActionWithImage(rightTimeActionForm, actionSelectedFile.value);
        } else {
          await editAction(rightTimeActionForm);
        }
        ElMessage.success('Action updated successfully');
      } else {
        if (actionSelectedFile.value) {
          await addActionWithImage(activeTripId.value, rightTimeActionForm, actionSelectedFile.value);
        } else {
          await addAction(activeTripId.value, rightTimeActionForm);
        }
        ElMessage.success('Action created successfully');
      }
      actionDialogVisible.value = false;
      await fetchActions(activeTripId.value);
    } catch (error) {
      if (error !== 'cancel') {
        ElMessage.error(`Failed to ${isEditingAction.value ? 'edit' : 'create'} action`);
        console.error(error);
      }
    }
  });
};

// Delete the action 删除行动
const deleteAction = async (action) => {
  try {
    await ElMessageBox.confirm(
        `Are you sure to delete action "${action.actionName}"?`,
        'Warning',
        { confirmButtonText: 'OK', cancelButtonText: 'Cancel', type: 'warning' }
    );
    await deleteActionById(action.id);
    ElMessage.success('Action deleted successfully');
    await fetchActions(activeTripId.value);
  } catch (error) {
    if (error !== 'cancel') {
      console.error(error);
    }
  }
};

onMounted(() => {
  fetchTrips();
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

.action-management {
  flex: 1;
  display: flex;
  height: auto;
  min-height: 600px;
}

.trip-list-container {
  flex: 0 0 300px;
  border-right: 1px solid #e6e6e6;
  background-color: #f5f7fa;
  padding: 20px;
  display: flex;
  flex-direction: column;
  min-height: 0;
}

.trip-list-header {
  margin-bottom: 15px;
}

.trip-scrollbar {
  flex: 1;
  min-height: 0;
  overflow-y: auto;
}

.trip-item {
  padding: 15px;
  margin-bottom: 10px;
  background-color: white;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s;
  border: 1px solid #ebeef5;
}

.trip-item:hover {
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.trip-item.active-trip {
  border-left: 4px solid #564a53;
  background-color: rgba(239, 225, 236, 0.23);
}

.trip-name {
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 8px;
  color: #303133;
}

.trip-dates, .trip-locations {
  font-size: 13px;
  color: #606266;
  margin-bottom: 5px;
  display: flex;
  align-items: center;
}

.trip-dates, .trip-locations {
  margin-right: 5px;
}

.action-main-container {
  flex: 1;
  padding: 20px;
  background-color: white;
  min-height: 0;
  overflow-y: auto;
}

.action-header {
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

.trip-pagination {
  margin-top: 10px;
  text-align: center;
}

</style>