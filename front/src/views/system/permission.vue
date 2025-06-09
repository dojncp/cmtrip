<template>
  <el-container class="permission-management">
    <el-header class="header two-line-header">
      <div class="header-top">
        <i class="el-icon-arrow-left"></i>
        <span @click="goBackToHomepage" style="cursor: pointer; color: #ccc;">← Back Home</span>
      </div>
      <div class="header-bottom"   style="display: flex; justify-content: space-between; align-items: center;">
        <span style="font-size: 25px; font-weight: bold;">Permission Management</span>
        <div style="display: flex; align-items: center;">
          <el-input
              v-model="searchKeyword"
              placeholder="Search by permission name"
              style="width: 240px;"
              clearable
              @clear="searchKeyword = ''"
          >
            <template #prefix>
              <el-icon><search /></el-icon>
            </template>
          </el-input>
          <el-button type="primary" @click="showAddPermissionDialog" style="float: right; margin-left: 10px;">
            Add Permission
          </el-button>
        </div>
      </div>
    </el-header>

    <el-main class="table-container">
      <el-table :data="paginatedPermissions" style="width: 100%">
        <el-table-column type="index" width="50"></el-table-column>
        <el-table-column prop="permissionName" label="Permission Name"></el-table-column>
        <el-table-column label="Operations">
          <template #default="{ row }">
            <el-button size="small" @click="editPermission(row)">Edit</el-button>
            <el-button size="small" type="danger" @click="deleteThisPermission(row)">Delete</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div style="margin-top: 20px; text-align: right;">
        <el-pagination
            background
            layout="total, sizes, prev, pager, next, jumper"
            :total="filteredPermissions.length"
            :page-sizes="pageSizes"
            :page-size="pageSize"
            :current-page="currentPage"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
        />
      </div>
    </el-main>

    <!-- 添加/编辑权限对话框 -->
    <el-dialog v-model="permissionDialog.visible" :title="permissionDialog.title" width="30%">
      <el-form :model="permissionForm" label-width="140px">
        <el-form-item label="Permission Name" required>
          <el-input
              v-model="permissionForm.permissionName"
              placeholder="Please enter permission name"
              :disabled="permissionDialog.title === 'Edit Permission'"
          ></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="permissionDialog.visible = false">Cancel</el-button>
          <el-button type="primary" @click="savePermission">Save</el-button>
        </span>
      </template>
    </el-dialog>
  </el-container>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue';
import {
  getPermissionList,
  addPermission,
  deletePermission,
  editPermission as editPermissionApi
} from '@/api/permission';
import { ElMessage, ElMessageBox } from 'element-plus';
import router from "@/router";
import {Search} from "@element-plus/icons-vue";

// Pagination related 分页相关
const currentPage = ref(1);
const pageSize = ref(10);
const pageSizes = [5, 10, 20, 50]; // 每页可选项
const paginatedPermissions = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value;
  const end = start + pageSize.value;
  return filteredPermissions.value.slice(start, end);
});
const handleSizeChange = (val) => {
  pageSize.value = val;
  currentPage.value = 1; // 重置到第一页
};
const handleCurrentChange = (val) => {
  currentPage.value = val;
};

const permissions = ref([]);
const searchKeyword = ref('');

const filteredPermissions = computed(() => {
  if (!searchKeyword.value) return permissions.value;
  return permissions.value.filter(permission =>
      permission.permissionName.toLowerCase().includes(searchKeyword.value.toLowerCase()))
});

// Permission dialog data 权限对话框数据
const permissionDialog = reactive({
  visible: false,
  title: 'Add Permission',
  isEdit: false,
  currentId: null
});

const permissionForm = reactive({
  permissionName: ''
});

const goBackToHomepage = () => {
  router.push("/home");
}

const fetchPermissionList = async () => {
  try {
    const response = await getPermissionList();
    permissions.value = response.data.data;
  } catch (error) {
    console.error('Permission list retrieve failed!', error);
  }
};

const showAddPermissionDialog = () => {
  permissionDialog.visible = true;
  permissionDialog.title = 'Add Permission';
  permissionDialog.isEdit = false;
  permissionForm.permissionName = '';
};

const editPermission = (permission) => {
  permissionDialog.visible = true;
  permissionDialog.title = 'Edit Permission';
  permissionDialog.isEdit = true;
  permissionDialog.currentId = permission.id;
  permissionForm.permissionName = permission.permissionName;
};

const savePermission = async () => {
  if (!permissionForm.permissionName.trim()) {
    ElMessage.warning('Please enter permission name');
    return;
  }
  try {
    if (permissionDialog.isEdit) {
      await editPermissionApi({
        id: permissionDialog.currentId,
        permissionName: permissionForm.permissionName
      });
      ElMessage.success('Permission updated successfully');
    } else {
      await addPermission({
        permissionName: permissionForm.permissionName
      });
      ElMessage.success('Permission added successfully');
    }
    permissionDialog.visible = false;
    await fetchPermissionList();
  } catch (error) {
    console.error('Permission save failed!', error);
  }
};

const deleteThisPermission = async (permission) => {
  try {
    await ElMessageBox.confirm(
        `Are you sure to delete permission "${permission.permissionName}"?`,
        'Warning',
        {
          confirmButtonText: 'OK',
          cancelButtonText: 'Cancel',
          type: 'warning',
        }
    );

    await deletePermission(permission.id);
    ElMessage.success('Permission deleted successfully');
    await fetchPermissionList();
  } catch (error) {
    if (error !== 'cancel') {
      console.error('Permission delete failed!', error);
    }
  }
};

onMounted(() => {
  fetchPermissionList();
});
</script>

<style scoped>
.permission-management {
  height: 100%;
}

.header {
  /* padding-bottom: 20px; */
}

.table-container {
  padding: 20px;
  background-color: #fff;
  border-radius: 5px;
  /* box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12); */
}

.ml-2 {
  margin-left: 8px;
}

.text-center {
  text-align: center;
  color: #999;
  padding: 20px 0;
}

.two-line-header {
  display: flex;
  flex-direction: column;
  padding: 20px;
  min-height: 100px;
}

.header-top {
  display: flex;
  align-items: center;
  margin-bottom: 15px;
}

.header-top .el-icon-arrow-left {
  margin-right: 8px;
}

.header-bottom {
  line-height: 1.2;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>