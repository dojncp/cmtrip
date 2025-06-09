<template>
  <el-container class="pass-management">

    <el-header class="header two-line-header">
      <div class="header-top">
        <i class="el-icon-arrow-left"></i>
        <span @click="goBackToHomepage" style="cursor: pointer; color: #ccc;">← Back Home</span>
      </div>
      <div class="header-bottom" style="display: flex; justify-content: space-between; align-items: center;">
        <span style="font-size: 25px; font-weight: bold;">Pass Management</span>
        <div style="display: flex; align-items: center;">
          <el-input
              v-model="searchQuery"
              placeholder="Search by pass name"
              style="width: 240px; margin-right: 20px;"
              clearable
              @clear="searchQuery = ''"
              @keyup.enter="handleSearch"
          >
            <template #prefix>
              <el-icon><search /></el-icon>
            </template>
          </el-input>
          <el-button
              v-if="userPermissions.indexOf('add-pass')>-1"
              type="primary"
              @click="openAddDialog()">
            New Pass
          </el-button>
        </div>
      </div>
    </el-header>

    <el-main class="table-container">
      <div v-if="paginatedPasses && paginatedPasses.length > 0">
        <el-table :data="paginatedPasses" style="width: 100%">
          <el-table-column type="index" width="50"></el-table-column>
          <el-table-column prop="passName" label="Pass Name"></el-table-column>
          <el-table-column prop="issueCompany" label="Issue Company"></el-table-column>
          <el-table-column prop="validDays" label="Valid Days"></el-table-column>
          <el-table-column prop="fare" label="Fare"></el-table-column>
          <el-table-column prop="fareCurrency" label="Currency"></el-table-column>
          <el-table-column label="Description">
            <template #default="{ row }">
              <el-button v-if="row.description" size="small" @click="showDescription(row)" style="height: auto">
                View <br/> Description
              </el-button>
              <span v-else> </span>
            </template>
          </el-table-column>
          <el-table-column label="Remark">
            <template #default="{ row }">
              <el-button v-if="row.remark" size="small" @click="showRemark(row)" style="height: auto">View <br/> Remark</el-button>
              <span v-else> </span>
            </template>
          </el-table-column>
          <el-table-column label="Image">
            <template #default="{ row }">
              <el-image
                  v-if="row.imgPath"
                  :src="getFullImageUrl(row.imgPath)"
                  style="width: 100px; height: 100px;"
                  fit="cover"
                  :preview-src-list="[getFullImageUrl(row.imgPath)]"
                  :append-to-body="true"
                  :preview-teleported="true"
              />
            </template>
          </el-table-column>
          <el-table-column label="Pass Controller">
            <template #default="{ row }">
              <div style="display: flex; flex-direction: column; align-items: center; gap: 5px;">
                <el-button v-if="userPermissions.indexOf('edit-pass')>-1" size="small" @click="openEditDialog(row)">Edit</el-button>
                <el-button v-if="userPermissions.indexOf('delete-pass')>-1" size="small" type="danger" @click="deletePass(row)">Delete</el-button>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="Entity Controller" width="150px">
            <template #default="{ row }">
              <div style="display: flex; flex-direction: column; align-items: center; gap: 5px;">
                <el-button v-if="userPermissions.indexOf('add-entity')>-1" size="small" type="primary" @click="openAddEntityDialog(row)">+ Entity</el-button>
                <el-button v-if="userPermissions.indexOf('list-my-entities')>-1" size="small" type="" @click="openEntityDetailDialog(row)">Entity Details</el-button>
              </div>
            </template>
          </el-table-column>
        </el-table>
        <!-- Pagination component 分页组件 -->
        <el-pagination
            class="pagination"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="currentPage"
            :page-sizes="[5, 10, 20, 50]"
            :page-size="pageSize"
            layout="total, sizes, prev, pager, next, jumper"
            :total="filteredPasses.length"
        />
      </div>
      <div v-else class="empty-state">
        <el-empty description="No certain data" />
      </div>

      <!-- Add or edit a pass 新增或编辑通票 -->
      <el-dialog v-model="dialogVisible" :title="isEditing ? 'Edit Pass' : 'Add Pass'" width="500px">
        <el-form ref="formRef" :rules="rules" :model="newPassForm" label-width="150px">
          <el-form-item label="Name" prop="passName">
            <el-input v-model="newPassForm.passName" />
          </el-form-item>
          <el-form-item label="Issue Company" prop="issueCompany">
            <el-input v-model="newPassForm.issueCompany" />
          </el-form-item>
          <el-form-item label="Valid Days" prop="validDays">
            <el-input-number
                v-model="newPassForm.validDays"
                :min="1"
                :max="366"
                :step="1"
                controls-position="right"
            />
          </el-form-item>
          <el-form-item label="Fare" prop="fare">
            <el-input-number
                v-model="newPassForm.fare"
                :min="0"
                :step="0.1"
                controls-position="right"
            />
          </el-form-item>
          <el-form-item label="Fare Currency" prop="fareCurrency">
            <el-select v-model="newPassForm.fareCurrency" placeholder="Select">
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
            <el-input
                v-model="newPassForm.description"
                type="textarea"
                rows="3"
                placeholder="Enter description if any"
            />
          </el-form-item>
          <el-form-item label="Image Upload" required>
            <el-upload
                ref="imgPath"
                action=""
                :auto-upload="false"
                :on-change="beforeUpload"
                v-model:file-list="fileList"
                :show-file-list="false"
            >
              <el-button
                  type="primary"
                  style="white-space: normal; line-height: 1.5; height: auto; padding: 5px 10px;"
              >
                Click here to upload<br> the pass image
              </el-button>
            </el-upload>
          </el-form-item>
          <el-form-item>
            <div class="el-upload__tip">Image files with a size less than 10MB</div>
          </el-form-item>
          <el-form-item>
            <!-- Image preview area, using el-image. Modify based on whether it is an edit or an add action
            图片预览区域，使用 el-image。根据 修改 还是 添加 而修改。 -->
            <el-image
                v-if="previewUrl"
                :src="previewUrl"
                fit="contain"
            />
            <el-image
                v-if="isEditing && newPassForm.imgPath && !selectedFile"
                :src="getFullImageUrl(newPassForm.imgPath)"
                fit="contain"
            />
          </el-form-item>
          <el-form-item>
            <div slot="tip" class="el-upload__tip" style="color: red">
              Just click the button "Upload Image" to upload a new image.
            </div>
          </el-form-item>
          <el-form-item label="Remark" prop="remark">
            <el-input
                v-model="newPassForm.remark"
                type="textarea"
                rows="3"
                placeholder="Enter remark if any"
            />
          </el-form-item>
        </el-form>
        <template #footer>
        <span class="dialog-footer">
          <el-button @click="cancel">Cancel</el-button>
          <el-button type="primary" @click="submitPass">Submit</el-button>
        </span>
        </template>
      </el-dialog>

      <!-- Add an entity 新增实体 -->
      <el-dialog v-model="newEntityDialogVisible" :title="'New Entity of ' + currentPassName" width="500px">
        <el-form ref="entityFormRef" :rules="newEntityRules" :model="newEntityForm" label-width="150px">
          <el-form-item label="Name" prop="entityName">
            <el-input v-model="newEntityForm.entityName" />
          </el-form-item>
          <el-form-item label="Start Date" prop="passStartDate">
            <el-date-picker v-model="newEntityForm.passStartDate" type="date" placeholder="Pick the date" />
          </el-form-item>
          <el-form-item label="End Date" prop="passEndDate">
            <el-date-picker disabled v-model="newEntityForm.passEndDate" type="date" placeholder="The end date would be calculated automatically" />
          </el-form-item>
          <el-form-item label="Description" prop="description">
            <el-input
                v-model="newEntityForm.description"
                type="textarea"
                rows="3"
                placeholder="Enter description if any"
            />
          </el-form-item>
          <el-form-item label="Remark" prop="remark">
            <el-input
                v-model="newEntityForm.remark"
                type="textarea"
                rows="3"
                placeholder="Enter remarks if any"
            />
          </el-form-item>
        </el-form>
        <template #footer>
        <span class="dialog-footer">
          <el-button @click="entityCancel">Cancel</el-button>
          <el-button type="primary" @click="submitNewEntity">Submit</el-button>
        </span>
        </template>
      </el-dialog>

      <!-- Remark popup 备注弹窗 -->
      <el-dialog v-model="remarkDialogVisible" :title="'Remark of ' + currentPassName" width="500px">
        <div style="white-space: pre-line;">{{ currentRemark }}</div>
        <template #footer>
          <el-button type="primary" @click="remarkDialogVisible = false">Close</el-button>
        </template>
      </el-dialog>

    </el-main>
  </el-container>
</template>

<script setup>
import {onMounted, ref} from 'vue';
import { Search } from '@element-plus/icons-vue';
import { useRouter } from 'vue-router';
import { ElMessage, ElMessageBox } from 'element-plus';
import {getFullImageUrl, formatDateWithoutTimezone} from "@/utils/utils";
import { computed } from 'vue';
import { watch } from 'vue';
import {addPass, addPassWithImage, deletePassById, editPass, editPassWithImage, getAllPasses} from "@/api/pass";

const loading = ref(false);
const router = useRouter();

// permissions 用户权限
const userPermissions = ref([]);

// Description related 描述相关
const descriptionDialogVisible = ref(false);
const currentDescription = ref('');
const currentPassName = ref('');
// Show description 显示描述
const showDescription = (row) => {
  currentDescription.value = row.description;
  currentPassName.value = row.passName;
  descriptionDialogVisible.value = true;
}

// Pagination related 分页相关
const currentPage = ref(1); // Current page 当前页码
const pageSize = ref(10);   // Items per page 每页条数
const total = ref(0);       // Total items 总条数
// Change in items per page 每页条数变化
const handleSizeChange = (val) => {
  pageSize.value = val;
};
// Change in current page number 当前页码变化
const handleCurrentChange = (val) => {
  currentPage.value = val;
};
// Search query 搜索查询
const searchQuery = ref('');
// Handle search 处理搜索
const handleSearch = () => {
  currentPage.value = 1;
};
// Computed property including search functionality 计算属性，包含搜索功能
const filteredPasses = computed(() => {
  if (!searchQuery.value) {
    return passes.value;
  }
  return passes.value.filter(pass =>
      pass.passName.toLowerCase().includes(searchQuery.value.toLowerCase())
  );
});
// Pagination data, computed property 分页数据，计算属性
const paginatedPasses = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value;
  const end = start + pageSize.value;
  return filteredPasses.value.slice(start, end);
});

// Submit the form 提交表单
const formRef = ref();
// Pass list data, initially an empty array 通票列表数据，初始为空数组
const passes = ref([]);
// Control the display of the "Add New Travel" dialog 控制新增旅行对话框的显示
const dialogVisible = ref(false);
// Mark whether it is edit mode 标记是否是编辑模式
const isEditing = ref(false);

// Form data for adding or editing a pass 新增或编辑通票的表单数据
const newPassForm = ref({
  passName: '',
  issueCompany: '',
  validDays: null,
  fare: null,
  fareCurrency: '',
  imgPath: '',
  description: '',
  remark: ''
});

// Form validation 表单校验
const rules = {
  // trigger: "blur" triggers on losing focus, while trigger: "change" triggers after a value change
  // trigger：blur是失焦时触发，change是修改后触发
  passName: [
    { required: true, message: 'Please input the pass name', trigger: 'blur' }
  ],
  issueCompany: [
    { required: true, message: 'Please input the issue company', trigger: 'blur' }
  ],
  validDays: [
    { required: true, message: 'Please input or select the valid days', trigger: 'blur' }
  ],
  fare: [
    { required: true, message: 'Please input or select the fare', trigger: 'blur'}
  ],
  fareCurrency: [
    { required: true, message: 'Please select the currency', trigger: 'blur'}
  ]
}

// Image preview URL 图片预览url
const previewUrl = ref(null);
// The file selected 选中文件
const selectedFile = ref(null);
// Name of the file selected 选中文件名
const selectedFileName = ref(null);
// List of files 文件列表
const fileList = ref([]);

// Remark-related 备注相关
const remarkDialogVisible = ref(false);
const currentRemark = ref('');

// Display the remark 显示备注
const showRemark = (row) => {
  currentRemark.value = row.remark;
  currentPassName.value = row.passName;
  remarkDialogVisible.value = true;
};

// Validation before file selection 选择文件前的校验
const beforeUpload = (file, fileList) => {
  // Limit to uploading only one file, so perform replacement 限制在只能上传1个文件，所以替换
  if (fileList.length > 1) {
    fileList.splice(0, 1);
  }
  // Get the file extension 获取文件后缀名
  const isImage = /\.(jpg|jpeg|png|gif|bmp|tiff|webp|heif)$/i.test(file.name);
  if (!isImage) {
    ElMessage.error('Only image type files can be uploaded!');
    return false; // 阻止上传
  }
  if (file.size > 10 * 1024 * 1024) {
    ElMessage.error('Image file size cannot exceed 10MB!');
    return false;
  }
  // Normal upload logic after file format validation passed 通过文件格式校验后的正常上传逻辑
  selectedFile.value = file.raw; // This is the binary file that can be sent to the backend 这才是能返回给后端的二进制文件
  selectedFileName.value = file.name;
  // Create a file preview URL 创建文件预览URL
  const reader = new FileReader();
  reader.onload = (e) => {
    previewUrl.value = e.target.result; // Get the preview URL 获取预览URL
  };
  reader.readAsDataURL(selectedFile.value); // Read the file as a Data URL 将文件读取为 Data URL 形式
};

// Clear form function 清空表单函数
const resetForm = () => {
  // Clear the form 清空表单
  newPassForm.value = {
    passName: '',
    issueCompany: '',
    validDays: null,
    fare: null,
    fareCurrency: '',
    imgPath: '',
    description: '',
    remark: ''
  };
  // Clear all variables related to files 清空所有与文件相关的变量
  previewUrl.value = null;
  selectedFile.value = null;
  selectedFileName.value = null;
  fileList.value = [];
}

// Cancel Button 退出按钮
const cancel = () => {
  dialogVisible.value = false;
  resetForm();
}

// Back to the homepage 返回首页
const goBackToHomepage = () => {
  router.push("/home");
}

// Get the pass list 获取通票列表
const fetchPassList = async () => {
  loading.value = true;
  try {
    const response = await getAllPasses();
    passes.value = response.data.data;
  } catch (error) {
    console.error('Retrieve passes failed! ', error);
  }
  loading.value = false;
};

// Open the add new dialog 打开新增对话框
const openAddDialog = () => {
  // Clear the form 清空表单
  resetForm();
  isEditing.value = false;
  dialogVisible.value = true;
}

// Open the edit dialog 打开编辑对话框
const openEditDialog = (row) => {
  isEditing.value = true;
  // Fill the form data 填充表单数据
  newPassForm.value = { ...row };
  dialogVisible.value = true;
};

// Submit to add or edit a new pass 提交新增或编辑新的通票
const submitPass = async () => {
  formRef.value.validate(async (valid) => {
    if (!valid) {
      ElMessage.error('Please fill in the required fields.')
      return
    }
    // Submit 提交
    try {
      if (isEditing.value) {
        // The file has been uploaded 上传了文件
        if (selectedFile.value) {
          await editPassWithImage(newPassForm.value, selectedFile.value)
        } else {
          await editPass(newPassForm.value)
        }
        ElMessage.success("Edited successfully!")
      } else {
        if (selectedFile.value) {
          await addPassWithImage(newPassForm.value, selectedFile.value);
        } else {
          await addPass(newPassForm.value);
        }
        ElMessage.success("Pass added successfully!")
      }
      dialogVisible.value = false;
      // Reload the pass list 重新加载通票列表
      await fetchPassList();
      resetForm();
    } catch (error) {
      ElMessage.error('Failed to submit the pass! ', error.message);
    }
  })
};

// Delete the pass 删除通票
const deletePass = async (row) => {
  try {
    await ElMessageBox.confirm(
        `Do you want to delete the pass "${row.passName}"?`,
        'Warning',
        {
          confirmButtonText: 'OK',
          cancelButtonText: 'CANCEL',
          type: 'warning',
        }
    );
    // The user clicked confirm, execute the delete logic 用户点击了确认，执行删除逻辑
    await deletePassById(row.id);
    ElMessage.success('Pass deleted successfully!');
    // Reload the pass list 重新加载通票列表
    await fetchPassList();
  } catch (error) {
    // The user clicked cancel or another error occurred 用户点击了取消或发生了其他错误
    if (error === 'cancel') {
      ElMessage.info('Pass delete cancelled!');
    } else {
      console.error('Pass delete failed! ', error);
    }
  }
};

// Navigate to the entity detail page of the corresponding pass 跳转到对应pass的entity详情页
const openEntityDetailDialog = (row) => {
  router.push({
    path: '/pass-entity',
    query: { passId: row.id }  // Include the passId parameter 携带passId参数
  });
};

// Lifecycle hook, executed after the component is mounted 生命周期钩子，在组件挂载后执行
onMounted(() => {
  // Load permissions 用户权限加载
  const permissions = localStorage.getItem('permissions');
  userPermissions.value = permissions ? JSON.parse(permissions) : [];
  // Fetch passes 拉取通票信息
  fetchPassList();
});

/* Below is the code for the "Add Entity" part 以下为新增entity部分代码 */
// Save the current row’s id as passId 保存当行id作为passId
const passId = ref(null);
const currentPassValidDays = ref(0);

// New entity form data 新增通票实体表单数据
const newEntityForm = ref({
  entityName: '',
  passStartDate: null,
  passEndDate: null,
  description: '',
  remark: ''
});

// Watch the change of passStartDate 引入watch以监视passStartDate的变化
watch(() => newEntityForm.value.passStartDate, (newStartDate) => {
  if (newStartDate && currentPassValidDays.value > 0) {
    // 计算 end date
    const endDate = new Date(newStartDate);
    endDate.setDate(endDate.getDate() + currentPassValidDays.value - 1);
    newEntityForm.value.passEndDate = endDate;
  } else {
    newEntityForm.value.passEndDate = null;
  }
});

// Submit the entity form 提交通票实体表单
const entityFormRef = ref();
// Control the visibility of the add entity dialog 控制新增通票实体对话框的显示
const newEntityDialogVisible = ref(false);

// Entity form validation 实体表单校验
const newEntityRules = {
  entityName: [
    { required: true, message: 'Please input the entity name', trigger: 'blur' }
  ],
  passStartDate: [
    { required: true, message: 'Please select the start date', trigger: 'blur' }
  ],
  passEndDate: [
    { required: true, message: 'Please select the end date', trigger: 'blur' }
  ],
}

// Function to clear the entity form 清空实体表单函数
const resetEntityForm = () => {
  newEntityForm.value = {
    entityName: '',
    passStartDate: null,
    passEndDate: null,
    description: '',
    remark: ''
  };
  // Clear all variables related to files 清空所有与文件相关的变量
  previewUrl.value = null;
  selectedFile.value = null;
  selectedFileName.value = null;
  fileList.value = [];
  // Clear the passId 清空passId
  passId.value = null;
  currentPassName.value = '';
}

// Cancel button of new entity 新增实体退出按钮
const entityCancel = () => {
  newEntityDialogVisible.value = false;
  resetEntityForm();
}

// Open the add entity dialog 打开新增实体对话框
const openAddEntityDialog = (row) => {
  newEntityDialogVisible.value = true;
  // Assign the current row's id to passId 给passId赋值当行id
  passId.value = row.id;
  currentPassName.value = row.passName;
  currentPassValidDays.value = row.validDays;
}

// Submit the new entity 提交新的实体
const submitNewEntity = async() => {
  entityFormRef.value.validate(async (valid) => {
    if (!valid) {
      ElMessage.error('Please fill in all supposed items!');
      return;
    }
    try {
      // Fix the time 固化时间
      const rightTimeEntityForm = {
        ...newEntityForm.value,
        passStartDate: formatDateWithoutTimezone(newEntityForm.value.passStartDate),
        passEndDate: formatDateWithoutTimezone(newEntityForm.value.passEndDate),
      };
      if (selectedFile.value) {
        await addEntityWithImage(passId.value, rightTimeEntityForm.value, selectedFile.value);
      } else {
        await addEntity(passId.value, rightTimeEntityForm.value);
      }
      ElMessage.success("New entity created successfully!");
      newEntityDialogVisible.value = false;
      // Reload the pass list 重新加载通票列表
      await fetchPassList();
      // Clear the entity form 清空实体表单
      resetEntityForm();
    } catch (error) {
      ElMessage.error('Failed to submit the new entity! ', error.message);
    }
  })
}

</script>

<style scoped>
.pass-management {
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
}

.pagination {
  margin-top: 20px;
  text-align: right;
}

.pagination {
  margin-top: 20px;
  text-align: right;
}
</style>