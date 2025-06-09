<template>
  <el-container class="role-management">
    <el-header class="header two-line-header">
      <div class="header-top">
        <i class="el-icon-arrow-left"></i>
        <span @click="goBackToHomepage" style="cursor: pointer; color: #ccc;">← Back Home</span>
      </div>
      <div class="header-bottom"  style="display: flex; justify-content: space-between; align-items: center;">
        <span style="font-size: 25px; font-weight: bold;">Role Management</span>
        <div style="display: flex; align-items: center;">
          <el-input
              v-model="searchKeyword"
              placeholder="Search by role name"
              style="width: 240px;"
              clearable
              @clear="searchKeyword = ''"
          >
            <template #prefix>
              <el-icon><search /></el-icon>
            </template>
          </el-input>
          <el-button type="primary" @click="showAddRoleDialog" style="float: right; margin-left: 10px;">
            Add Role
          </el-button>
        </div>
      </div>
    </el-header>

    <el-main class="table-container">
      <el-table :data="paginatedRoles" style="width: 100%">
        <el-table-column type="index" width="50"></el-table-column>
        <el-table-column prop="roleName" label="Role Name"></el-table-column>
        <el-table-column label="Permissions">
          <template #default="{ row }">
            <el-button size="small" @click="showRolePermissions(row)">
              Check
            </el-button>
            <el-button type="primary" size="small" @click="editRolePermissions(row)">
              Edit
            </el-button>
          </template>
        </el-table-column>
        <el-table-column label="Operations">
          <template #default="{ row }">
            <el-button size="small" @click="editRole(row)">Edit</el-button>
            <el-button size="small" type="danger" @click="deleteThisRole(row)">Delete</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div style="margin-top: 20px; text-align: right;">
        <el-pagination
            background
            layout="total, sizes, prev, pager, next, jumper"
            :total="filteredRoles.length"
            :page-sizes="pageSizes"
            :page-size="pageSize"
            :current-page="currentPage"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
        />
      </div>
    </el-main>

    <!-- Add/Edit Role Dialog 添加/编辑角色对话框 -->
    <el-dialog v-model="roleDialog.visible" :title="roleDialog.title" width="30%">
      <el-form :model="roleForm" label-width="140px">
        <el-form-item label="Role Name" required>
          <el-input
              v-model="roleForm.roleName"
              placeholder="Please enter role name"
              :disabled="roleDialog.title === 'Edit Role'"
          ></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="roleDialog.visible = false">Cancel</el-button>
          <el-button type="primary" @click="saveRole">Save</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- Role Permission Edit Dialog 角色权限编辑对话框 -->
    <el-dialog v-model="permissionEditDialog.visible" :title="`Edit Permissions for ${permissionEditDialog.roleName}`" width="50%">
      <el-scrollbar>
        <div class="permission-edit-container">
          <!-- Add "Select All" button 添加全选按钮 -->
          <div class="select-all-header">
            <el-checkbox
                v-model="permissionEditDialog.allSelected"
                :indeterminate="permissionEditDialog.isIndeterminate"
                @change="handleSelectAllPermissionsChange"
                class="select-all-checkbox"
            >
              Select all on this page
            </el-checkbox>
          </div>
          <el-checkbox-group v-model="permissionEditDialog.selectedPermissions" @change="handlePermissionSelectionChange">
            <!-- Paginate the display of permission items 分页显示权限项 -->
            <div v-for="permission in paginatedPermissions" :key="permission.id" class="permission-item">
              <el-checkbox
                  :label="permission.id"
                  :class="{
                    'original-permission': permissionEditDialog.originalPermissions.includes(permission.id),
                    'new-permission': !permissionEditDialog.originalPermissions.includes(permission.id)
                  }"
              >
                {{ permission.permissionName }}
              </el-checkbox>
            </div>
          </el-checkbox-group>
          <!-- Add pagination controls 添加分页控件 -->
          <div style="margin-top: 20px; text-align: center;">
            <el-pagination
                small
                layout="prev, pager, next"
                :total="permissionEditDialog.allPermissions.length"
                :page-size="permissionEditDialog.pageSize"
                :current-page="permissionEditDialog.currentPage"
                @current-change="handlePermissionPageChange"
            />
          </div>
        </div>
      </el-scrollbar>
      <template #footer>
    <span class="dialog-footer">
      <el-button @click="permissionEditDialog.visible = false">Cancel</el-button>
      <el-button type="primary" @click="savePermissionChanges">Save</el-button>
    </span>
      </template>
    </el-dialog>

    <!-- Role Permission View Dialog 角色权限查看对话框 -->
    <el-dialog v-model="permissionViewDialog.visible" :title="`Permissions of ${permissionViewDialog.roleName}`" width="30%">
      <el-scrollbar>
        <el-tag v-for="(item, index) in permissionViewDialog.permissions" :key="index" class="ml-2">{{ item }}</el-tag>
        <div v-if="!permissionViewDialog.permissions.length" class="text-center">No Permissions Yet</div>
      </el-scrollbar>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="permissionViewDialog.visible = false">Close</el-button>
        </span>
      </template>
    </el-dialog>
  </el-container>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue';
import {
  getRoleList,
  addRole,
  deleteRole,
  updateRolePermissions,
  getRolePermissionNames
} from '@/api/role';
import { getPermissionList } from '@/api/permission';
import { ElMessage, ElMessageBox } from 'element-plus';
import router from "@/router";
import {Search} from "@element-plus/icons-vue";

// Pagination related 分页相关
const currentPage = ref(1);
const pageSize = ref(10);
const pageSizes = [5, 10, 20, 50]; // Items per page options 每页可选项
const paginatedRoles = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value;
  const end = start + pageSize.value;
  return filteredRoles.value.slice(start, end);
});
const handleSizeChange = (val) => {
  pageSize.value = val;
  currentPage.value = 1; // Reset to the first page 重置到第一页
};
const handleCurrentChange = (val) => {
  currentPage.value = val;
};

const roles = ref([]);
const searchKeyword = ref('');

const filteredRoles = computed(() => {
  if (!searchKeyword.value) return roles.value;
  return roles.value.filter(role =>
      role.roleName.toLowerCase().includes(searchKeyword.value.toLowerCase()))
});

// Character dialog data 角色对话框数据
const roleDialog = reactive({
  visible: false,
  title: 'Add Role',
  isEdit: false,
  currentId: null
});

const roleForm = reactive({
  roleName: ''
});

// Permission editing dialog data 权限编辑对话框数据
const permissionEditDialog = reactive({
  visible: false,
  roleName: '',
  roleId: null,
  allPermissions: [], // All valid permissions 所有可用权限
  originalPermissions: [], // Original user role 用户原始角色
  selectedPermissions: [], // Currently selected permissions 当前选中的权限
  currentPage: 1,      // Current page number 当前页码
  pageSize: 10,        // Number of items per page 每页显示数量
  allSelected: false,  // Full selection status 全选状态
  isIndeterminate: false // Indeterminate state 不确定状态
});

// Computed property - paginated permission data 计算属性 - 分页后的权限数据
const paginatedPermissions = computed(() => {
  const start = (permissionEditDialog.currentPage - 1) * permissionEditDialog.pageSize;
  const end = start + permissionEditDialog.pageSize;
  return permissionEditDialog.allPermissions.slice(start, end);
});
// Handle permission pagination changes 处理权限分页变化
const handlePermissionPageChange = (val) => {
  permissionEditDialog.currentPage = val;
};

// Handle select all/deselect all permissions 处理全选/取消全选权限
const handleSelectAllPermissionsChange = (val) => {
  const currentPagePermissions = paginatedPermissions.value.map(permission => permission.id);
  if (val) {
    // Add all permissions on the current page to the selected list (deduplicated) 添加当前页所有权限到选中列表（去重）
    permissionEditDialog.selectedPermissions = Array.from(
        new Set([...permissionEditDialog.selectedPermissions, ...currentPagePermissions])
    );
  } else {
    // Remove all permissions on the current page from the selected list 从选中列表中移除当前页所有权限
    permissionEditDialog.selectedPermissions = permissionEditDialog.selectedPermissions.filter(
        id => !currentPagePermissions.includes(id)
    );
  }
  updateSelectAllPermissionsState();
};

// Handle single permission selection change 处理单个权限选择变化
const handlePermissionSelectionChange = () => {
  updateSelectAllPermissionsState();
};

// Update the full selection status 更新全选状态
const updateSelectAllPermissionsState = () => {
  const currentPagePermissions = paginatedPermissions.value.map(permission => permission.id);
  const selectedCount = currentPagePermissions.filter(id =>
      permissionEditDialog.selectedPermissions.includes(id)
  ).length;
  permissionEditDialog.allSelected = selectedCount === currentPagePermissions.length;
  permissionEditDialog.isIndeterminate = selectedCount > 0 && selectedCount < currentPagePermissions.length;
};

// Permission viewing dialog data 权限查看对话框数据
const permissionViewDialog = reactive({
  visible: false,
  roleName: '',
  permissions: []
});

// Return to the home page 返回首页
const goBackToHomepage = () => {
  router.push("/home");
}

// Get the role list 获取角色列表
const fetchRoleList = async () => {
  try {
    const response = await getRoleList();
    roles.value = response.data.data;
  } catch (error) {
    console.error('Role list retrieve failed! ', error);
  }
};

// Show the add role dialog box 显示添加角色对话框
const showAddRoleDialog = () => {
  roleDialog.visible = true;
  roleDialog.title = 'Add Role';
  roleDialog.isEdit = false;
  roleForm.roleName = '';
};

// Edit the role 编辑角色
const editRole = (role) => {
  roleDialog.visible = true;
  roleDialog.title = 'Edit Role';
  roleDialog.isEdit = true;
  roleDialog.currentId = role.id;
  roleForm.roleName = role.roleName;
};

// Save the role 保存角色
const saveRole = async () => {
  if (!roleForm.roleName.trim()) {
    ElMessage.warning('Please enter role name');
    return;
  }
  try {
    if (roleDialog.isEdit) {
      await addRole({
        id: roleDialog.currentId,
        roleName: roleForm.roleName
      });
      ElMessage.success('Role updated successfully');
    } else {
      await addRole({
        roleName: roleForm.roleName
      });
      ElMessage.success('Role added successfully');
    }
    roleDialog.visible = false;
    await fetchRoleList();
  } catch (error) {
    console.error('Role save failed! ', error);
  }
};

// Delete roles 删除角色
const deleteThisRole = async (role) => {
  try {
    await ElMessageBox.confirm(
        `Are you sure to delete role "${role.roleName}"?`,
        'Warning',
        {
          confirmButtonText: 'OK',
          cancelButtonText: 'Cancel',
          type: 'warning',
        }
    );
    await deleteRole(role.id);
    ElMessage.success('Role deleted successfully');
    await fetchRoleList();
  } catch (error) {
    if (error !== 'cancel') {
      console.error('Role delete failed! ', error);
    }
  }
};

// Display role permissions 显示角色权限
const showRolePermissions = async (role) => {
  try {
    const response = await getRolePermissionNames(role.id);
    permissionViewDialog.roleName = role.roleName;
    permissionViewDialog.permissions = response.data.data || [];
    permissionViewDialog.visible = true;
  } catch (error) {
    console.error('Retrieve role\'s permissions failed!', error);
  }
};

// Edit role permissions 编辑角色权限
const editRolePermissions = async (role) => {
  try {
    // Get all permissions 获取所有权限
    const permissionsResponse = await getPermissionList();
    permissionEditDialog.allPermissions = permissionsResponse.data.data || [];
    // Reset pagination to the first page 重置分页到第一页
    permissionEditDialog.currentPage = 1;
    // Get the current permissions of the role 获取角色当前权限
    const rolePermissionsResponse = await getRolePermissionNames(role.id);
    const rolePermissions = rolePermissionsResponse.data.data || [];
    // Initialize dialog data 初始化对话框数据
    permissionEditDialog.roleName = role.roleName;
    permissionEditDialog.roleId = role.id;
    // Match using permissionName 使用permissionName进行匹配
    permissionEditDialog.originalPermissions = permissionEditDialog.allPermissions
        .filter(permission => rolePermissions.includes(permission.permissionName))
        .map(permission => permission.id);
    permissionEditDialog.selectedPermissions = [...permissionEditDialog.originalPermissions];
    permissionEditDialog.visible = true;
  } catch (error) {
    console.error('Retrieve permissions failed! ', error);
  }
};

// Save permission changes 保存权限变更
const savePermissionChanges = async () => {
  try {
    // Calculate differences 计算差异
    const assignPermissionIds = permissionEditDialog.selectedPermissions
        .filter(id => !permissionEditDialog.originalPermissions.includes(id));
    const removePermissionIds = permissionEditDialog.originalPermissions
        .filter(id => !permissionEditDialog.selectedPermissions.includes(id));
    // Backup old data and prepare for optimistic update 备份旧数据，准备乐观更新
    const originalBackup = [...permissionEditDialog.originalPermissions];
    permissionEditDialog.originalPermissions = [...permissionEditDialog.selectedPermissions];
    // Show loading status 显示加载状态
    permissionEditDialog.loading = true;
    // Send differential request 发送差分请求
    await updateRolePermissions({
      roleId: permissionEditDialog.roleId,
      assignPermissionIds,
      removePermissionIds
    });
    // Update 更新
    ElMessage.success('Permissions updated successfully!');
    await fetchRoleList();
  } catch (error) {
    console.error('Permission update failed! ', error);
  } finally {
    permissionEditDialog.visible = false;
    permissionEditDialog.loading = false;
  }
};

onMounted(() => {
  fetchRoleList();
});
</script>

<style scoped>
.role-management {
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

.permission-edit-container {
  padding: 10px;
}

.permission-item {
  margin: 8px 0;
  padding: 8px;
  border-radius: 4px;
  transition: background-color 0.3s;
}

.permission-item:hover {
  background-color: #f5f7fa;
}

:deep(.original-permission .el-checkbox__input.is-checked .el-checkbox__inner) {
  background-color: #67c23a;
  border-color: #67c23a;
}

:deep(.new-permission .el-checkbox__input.is-checked .el-checkbox__inner) {
  background-color: #409eff;
  border-color: #409eff;
}

.select-all-header {
  position: absolute;
  top: 15px;
  right: 20px;
  z-index: 1;
}

.select-all-checkbox {
  font-weight: normal;
  color: #606266;
}

.select-all-checkbox .el-checkbox__label {
  font-size: 13px;
}

:deep(.el-dialog__header) {
  padding-bottom: 20px;
}

.permission-edit-container {
  position: relative;
  padding: 40px 10px 10px;
}

</style>