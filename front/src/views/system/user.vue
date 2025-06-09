<template>
  <el-container class="user-management">
    <el-header class="header two-line-header">
      <div class="header-top">
        <i class="el-icon-arrow-left"></i>
        <span @click="goBackToHomepage" style="cursor: pointer; color: #ccc;">← Back Home</span>
      </div>
      <div class="header-bottom" style="display: flex; justify-content: space-between; align-items: center;">
        <span style="font-size: 25px; font-weight: bold;">User Management</span>
        <el-input
            v-model="searchKeyword"
            placeholder="Search by username"
            style="width: 240px;"
            clearable
            @clear="searchKeyword = ''"
        >
          <template #prefix>
            <el-icon><search /></el-icon>
          </template>
        </el-input>
      </div>
    </el-header>

    <el-main class="table-container">
      <el-table :data="paginatedUsers" style="width: 100%">
      <el-table-column type="index" width="50"></el-table-column>
        <el-table-column prop="userName" label="Username"></el-table-column>
        <el-table-column prop="email" label="Email"></el-table-column>
        <el-table-column label="Role">
          <template #default="{ row }">
            <el-button size="small" @click="showRole(row)">
              Check
            </el-button>
            <el-button type="primary" size="small" @click="editRole(row, $event)" ref="permissionBtn">
              Edit
            </el-button>
          </template>
        </el-table-column>
        <el-table-column label="Permissions">
          <template #default="{ row }">
            <el-button size="small" @click="showPermissions(row, $event)" ref="permissionBtn">
              Check
            </el-button>
            <el-button type="primary" size="small" @click="editPermissions(row, $event)" ref="permissionBtn">
              Edit
            </el-button>
          </template>
        </el-table-column>
        <el-table-column label="Operations">
          <template #default="{ row }">
            <el-button size="small" @click="editUser(row)">Edit</el-button>
            <el-button size="small" type="danger" @click="deleteThisUser(row)">Delete</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div style="margin-top: 20px; text-align: right;">
        <el-pagination
            background
            layout="total, sizes, prev, pager, next, jumper"
            :total="filteredUsers.length"
            :page-sizes="pageSizes"
            :page-size="pageSize"
            :current-page="currentPage"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
        />
      </div>
    </el-main>

    <!-- User role edit dialog 用户角色编辑弹窗 -->
    <el-dialog v-model="roleEditDialog.visible" :title="`Edit Roles for ${roleEditDialog.userName}`" width="50%">
      <el-scrollbar>
        <div class="role-edit-container">
          <!-- Move select all button to top right corner 将全选按钮移到右上角 -->
          <div class="select-all-header">
            <el-checkbox
                v-model="roleEditDialog.allSelected"
                :indeterminate="roleEditDialog.isIndeterminate"
                @change="handleSelectAllChange"
                class="select-all-checkbox"
            >
              Select all on this page
            </el-checkbox>
          </div>
          <el-checkbox-group v-model="roleEditDialog.selectedRoles" @change="handleRoleSelectionChange">
            <div v-for="role in paginatedRoles" :key="role.id" class="role-item">
              <el-checkbox
                  :label="role.id"
                  :class="{
                'original-role': roleEditDialog.originalRoles.includes(role.id),
                'new-role': !roleEditDialog.originalRoles.includes(role.id)
              }"
              >
                {{ role.roleName }}
              </el-checkbox>
            </div>
          </el-checkbox-group>
          <!-- Add pagination control 添加分页控件 -->
          <div style="margin-top: 20px; text-align: center;">
            <el-pagination
                small
                layout="prev, pager, next"
                :total="roleEditDialog.allRoles.length"
                :page-size="roleEditDialog.pageSize"
                :current-page="roleEditDialog.currentPage"
                @current-change="handleRolePageChange"
            />
          </div>
        </div>
      </el-scrollbar>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="roleEditDialog.visible = false">Cancel</el-button>
          <el-button type="primary" @click="saveRoleChanges">Save</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- User details dialog 用户详情弹窗 -->
    <el-dialog v-model="detailPanel.visible" :title="detailPanel.title" width="30%">
      <el-scrollbar>
        <el-tag v-for="(item, index) in detailPanel.list" :key="index" class="ml-2">{{ item }}</el-tag>
        <div v-if="!detailPanel.list.length" class="text-center">No {{ detailPanel.type === 'role' ? 'Roles' : 'Permissions' }} Yet</div>
      </el-scrollbar>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="detailPanel.visible = false">Well</el-button>
        </span>
      </template>
    </el-dialog>
  </el-container>
</template>

<script setup>
import { Search } from '@element-plus/icons-vue'
import { ref, reactive, onMounted, computed } from 'vue';
import {getUserList, getUserRoleNames, getUserPermissionNames, deleteUser, updateUserRoles} from '@/api/user';
import { getRoleList } from '@/api/role';
import {ElMessage, ElMessageBox} from 'element-plus';
import router from "@/router";

// Pagination related variables 分页相关
const currentPage = ref(1);
const pageSize = ref(10);
const pageSizes = [5, 10, 20, 50];
const paginatedUsers = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value;
  const end = start + pageSize.value;
  return filteredUsers.value.slice(start, end);
});
const handleSizeChange = (val) => {
  pageSize.value = val;
  currentPage.value = 1; // Reset to first page 重置到第一页
};
const handleCurrentChange = (val) => {
  currentPage.value = val;
};

const filteredUsers = computed(() => {
  if (!searchKeyword.value) return users.value;
  return users.value.filter(user =>
      user.userName.toLowerCase().includes(searchKeyword.value.toLowerCase())
  );
});

// User list data 用户列表数据
const users = ref([]);
const searchKeyword = ref('');

// Details display panel (for both roles and permissions) 详情展示面板 (同时用于角色和权限)
const detailPanel = reactive({
  visible: false,
  title: '',
  list: [],
  userId: null,
  type: 'permission', // 'permission' or 'role' 'permission' 或 'role'
});

// Go back to homepage 返回首页
const goBackToHomepage = () => {
  router.push("/home");
}

// Fetch user list 获取用户列表
const fetchUserList = async () => {
  try {
    const response = await getUserList();
    users.value = response.data.data;
    // console.log(users.value);
  } catch (error) {
    console.error('Retrieve user list failed! ', error);
  }
};

// Fetch user roles 获取用户角色列表
const fetchUserRoles = async (id) => {
  try {
    const response = await getUserRoleNames(id);
    detailPanel.list = response.data.data;
  } catch (error) {
    console.error('Retrieve user\'s role failed! ', error);
  }
};

// Role edit dialog data 新增角色编辑对话框数据
const roleEditDialog = reactive({
  visible: false,
  userName: '',
  userId: null,
  allRoles: [], // All available roles 所有可用角色
  originalRoles: [], // User's original roles 用户原始角色
  selectedRoles: [], // Currently selected roles 当前选中的角色
  currentPage: 1,      // Current page number 当前页码
  pageSize: 10,       // Items per page 每页显示数量
  allSelected: false,  // Select all status 全选状态
  isIndeterminate: false // Indeterminate status 不确定状态
});

// Computed property - paginated roles 计算属性 - 分页后的角色数据
const paginatedRoles = computed(() => {
  const start = (roleEditDialog.currentPage - 1) * roleEditDialog.pageSize;
  const end = start + roleEditDialog.pageSize;
  return roleEditDialog.allRoles.slice(start, end);
});
// Handle role pagination change 处理角色分页变化
const handleRolePageChange = (val) => {
  roleEditDialog.currentPage = val;
};

// Handle select all/cancel all 处理全选/取消全选
const handleSelectAllChange = (val) => {
  const currentPageRoles = paginatedRoles.value.map(role => role.id);
  if (val) {
    // Add all roles on current page to selected list (deduplicate) 添加当前页所有角色到选中列表（去重）
    roleEditDialog.selectedRoles = Array.from(
        new Set([...roleEditDialog.selectedRoles, ...currentPageRoles])
    );
  } else {
    // Remove all roles on current page from selected list 从选中列表中移除当前页所有角色
    roleEditDialog.selectedRoles = roleEditDialog.selectedRoles.filter(
        id => !currentPageRoles.includes(id)
    );
  }
  updateSelectAllState();
};

// Handle single role selection change 处理单个角色选择变化
const handleRoleSelectionChange = () => {
  updateSelectAllState();
};

// Update select all status 更新全选状态
const updateSelectAllState = () => {
  const currentPageRoles = paginatedRoles.value.map(role => role.id);
  const selectedCount = currentPageRoles.filter(id =>
      roleEditDialog.selectedRoles.includes(id)
  ).length;

  roleEditDialog.allSelected = selectedCount === currentPageRoles.length;
  roleEditDialog.isIndeterminate = selectedCount > 0 && selectedCount < currentPageRoles.length;
};

// Edit role 编辑角色
const editRole = async (user) => {
  try {
    // Get all roles 获取所有角色
    const rolesResponse = await getRoleList();
    roleEditDialog.allRoles = rolesResponse.data.data || [];

    // Reset pagination to first page 重置分页到第一页
    roleEditDialog.currentPage = 1;

    // Get user's current roles 获取用户当前角色
    const userRolesResponse = await getUserRoleNames(user.id);
    const userRoles = userRolesResponse.data.data || [];

    // Initialize dialog data 初始化对话框数据
    roleEditDialog.userName = user.userName;
    roleEditDialog.userId = user.id;
    // Match using roleName 使用 roleName 进行匹配
    roleEditDialog.originalRoles = roleEditDialog.allRoles
        .filter(role => userRoles.includes(role.roleName))
        .map(role => role.id);
    // The above line changes originalRoles to each role's id, the selectedRoles below are the same 上一行将originalRoles变更为每个role的id，下一行的selectedRoles相同
    roleEditDialog.selectedRoles = [...roleEditDialog.originalRoles];

    roleEditDialog.visible = true;
  } catch (error) {
    console.error('Retrieve roles failed! ', error);
  }
};

// Frontend save logic 前端保存逻辑
const saveRoleChanges = async () => {
  try {
    // 1. Calculate differences: only keep ids, no longer convert to roleName 计算差异：只保留 id，不再转成 roleName
    const assignRoleIds = roleEditDialog.selectedRoles
        .filter(id => !roleEditDialog.originalRoles.includes(id));
    const removeRoleIds = roleEditDialog.originalRoles
        .filter(id => !roleEditDialog.selectedRoles.includes(id));
    console.log("调试：assignRoleIds=", assignRoleIds, "removeRoleIds=", removeRoleIds)
    // 2. Backup old data, prepare for optimistic update 备份旧数据，准备乐观更新
    const originalBackup = [...roleEditDialog.originalRoles];
    roleEditDialog.originalRoles = [...roleEditDialog.selectedRoles];
    // 3. Show loading state 显示加载状态
    roleEditDialog.loading = true;
    // 4. Send differential request 发送差分请求
    await updateUserRoles({
      userId: roleEditDialog.userId,
      assignRoleIds,
      removeRoleIds
    });
    // 5. Update successful 更新成功
    ElMessage.success('Roles updated successfully!');
    await fetchUserList();
  } catch (error) {
    // 6. Rollback on failure 失败时回滚
    roleEditDialog.originalRoles = originalBackup;
    roleEditDialog.selectedRoles = [...originalBackup];
    ElMessage.error(`Roles updated failed: ${error.message}`);
  } finally {
    roleEditDialog.visible = false;
    roleEditDialog.loading = false;
  }
};

// Fetch user permissions 获取用户权限列表
const fetchUserPermissions = async (id) => {
  try {
    const response = await getUserPermissionNames(id);
    detailPanel.list = response.data.data;
  } catch (error) {
    console.error('Retrieve user\'s permissions failed! ', error);
  }
};

// Handle click to view role 处理点击查看角色
const showRole = async (user) => {
  detailPanel.visible = true;
  detailPanel.title = `Roles of ${user.userName}`;
  detailPanel.userId = user.id;
  detailPanel.type = 'role';
  await fetchUserRoles(user.id);
};

// Handle click to view permissions 处理点击查看权限
const showPermissions = async (user, event) => {
  detailPanel.visible = true;
  detailPanel.title = `Permissions of ${user.userName}`;
  detailPanel.userId = user.id;
  detailPanel.type = 'permission';
  await fetchUserPermissions(user.id);
};

// Edit user (needs to be implemented by you) 编辑用户 (需要您实现)
const editUser = (user) => {
  console.log('编辑用户:', user);
  ElMessage('This operation is not in service.');
};

// Delete user 删除用户
const deleteThisUser = async(row) => {
  try {
    await ElMessageBox.confirm(
        'Do you want to delete the record of user ' + row.userName + '?',
        'Warning',
        {
          confirmButtonText: 'OK',
          cancelButtonText: 'CANCEL',
          type: 'warning',
        }
    );
    // User clicked confirm, execute delete logic 用户点击了确认，执行删除逻辑
    //console.log('确认删除用户 ID:', id);
    // Call your delete user API here 在这里调用您的删除用户 API
    const result = await deleteUser(row.id);
    ElMessage.success('Deleted successfully');
    // Reload user list or other update operations 重新加载用户列表或其他更新操作
    await fetchUserList();
  } catch (error) {
    // User clicked cancel or other error occurred 用户点击了取消或发生了其他错误
    if (error === 'cancel') {
      ElMessage.info('Delete cancelled');
    } else {
      console.log(error)
      ElMessage.error(error);
    }
  }
};

// Lifecycle hook - mounted 生命周期开始
onMounted(() => {
  fetchUserList();
});
</script>

<style scoped>
.user-management {
  height: 100%;
}

.header {
  //padding-bottom: 20px;
}

.table-container {
  padding: 20px;
  background-color: #fff;
  border-radius: 5px;
  //box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12);
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
  flex-direction: column; /* 将子元素垂直排列 */
  padding: 20px; /* 根据需要调整内边距 */
  min-height: 100px; /* 设置最小高度，确保有足够的空间容纳两行文字 */
}

.header-top {
  display: flex;
  align-items: center; /* 垂直居中 */
  margin-bottom: 15px; /* 上下两行之间的间距 */
}

.header-top .el-icon-arrow-left {
  margin-right: 8px;
}

.header-bottom {
  line-height: 1.2; /* 尝试增加这个值 */
}

/* 新增角色编辑样式 */
.role-edit-container {
  padding: 10px;
}

.role-item {
  margin: 8px 0;
  padding: 8px;
  border-radius: 4px;
  transition: background-color 0.3s;
}

.role-item:hover {
  background-color: #f5f7fa;
}

/* 原始角色选中样式 */
:deep(.original-role .el-checkbox__input.is-checked .el-checkbox__inner) {
  background-color: #67c23a;
  border-color: #67c23a;
}

/* 新选中角色样式 */
:deep(.new-role .el-checkbox__input.is-checked .el-checkbox__inner) {
  background-color: #409eff;
  border-color: #409eff;
}

/* 全选按钮头部样式 */
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

/* 调整对话框标题位置，为全选按钮留出空间 */
:deep(.el-dialog__header) {
  padding-bottom: 20px;
}

/* 角色编辑容器添加相对定位 */
.role-edit-container {
  position: relative;
  padding: 40px 10px 10px;
}

</style>