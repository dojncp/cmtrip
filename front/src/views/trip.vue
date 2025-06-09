<template>
  <el-container class="trip-management">

    <el-header class="header two-line-header">
      <div class="header-top">
        <i class="el-icon-arrow-left"></i>
        <span @click="goBackToHomepage" style="cursor: pointer; color: #ccc;">← Back Home</span>
      </div>
      <div class="header-bottom" style="display: flex; justify-content: space-between; align-items: center;">
        <span style="font-size: 25px; font-weight: bold;">Trip Management</span>
        <div style="display: flex; align-items: center;">
          <el-input
              v-model="searchQuery"
              placeholder="Search by trip name"
              style="width: 240px; margin-right: 20px;"
              clearable
              @clear="searchQuery = ''"
              @keyup.enter="handleSearch"
          >
            <template #prefix>
              <el-icon><search /></el-icon>
            </template>
          </el-input>
          <el-button type="primary" @click="openAddDialog()">New Trip</el-button>
        </div>
      </div>
    </el-header>

    <el-main class="table-container">
      <div v-if="paginatedTrips && paginatedTrips.length > 0">
        <el-table :data="paginatedTrips" style="width: 100%">
          <el-table-column type="index" width="50"></el-table-column>
          <el-table-column prop="tripName" label="Trip Name"></el-table-column>
          <el-table-column prop="tripStartDate" label="Start Date"></el-table-column>
          <el-table-column prop="tripEndDate" label="End Date"></el-table-column>
          <el-table-column prop="tripFrom" label="Departure"></el-table-column>
          <el-table-column prop="tripTo" label="Destination"></el-table-column>
          <el-table-column prop="tripBudget" label="Budget"></el-table-column>
          <el-table-column prop="tripBudgetCurrency" label="Currency"></el-table-column>
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
          <el-table-column label="Trip Controller">
            <template #default="{ row }">
              <div style="display: flex; flex-direction: column; align-items: center; gap: 5px;">
                <el-button size="small" @click="openEditDialog(row)">Edit</el-button>
                <el-button size="small" type="danger" @click="deleteTrip(row)">Delete</el-button>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="Action Controller" width="150px">
            <template #default="{ row }">
              <div style="display: flex; flex-direction: column; align-items: center; gap: 5px;">
                <el-button size="small" type="primary" @Click="openAddActionDialog(row)">+ Action</el-button>
                <el-button size="small" type="" @Click="openActionDetailDialog(row)">Action Details</el-button>
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
            :total="filteredTrips.length"
        />
      </div>
      <div v-else class="empty-state">
        <el-empty description="No certain data" />
      </div>

      <!-- Add or edit a trip 新增或编辑旅行 -->
      <el-dialog v-model="dialogVisible" :title="isEditing ? 'Edit Trip' : 'New Trip'" width="500px">
        <el-form ref="formRef" :rules="rules" :model="newTripForm" label-width="120px">
          <el-form-item label="Name" prop="tripName">
            <el-input v-model="newTripForm.tripName" />
          </el-form-item>
          <el-form-item label="Start Date" prop="tripStartDate">
            <el-date-picker v-model="newTripForm.tripStartDate" type="date" placeholder="Pick a date" />
          </el-form-item>
          <el-form-item label="End Date" prop="tripEndDate">
            <el-date-picker v-model="newTripForm.tripEndDate" type="date" placeholder="Pick a date" />
          </el-form-item>
          <el-form-item label="Departure" prop="tripFrom">
            <el-input v-model="newTripForm.tripFrom" />
          </el-form-item>
          <el-form-item label="Destination" prop="tripTo">
            <el-input v-model="newTripForm.tripTo" />
          </el-form-item>
          <el-form-item label="Budget" prop="tripBudget">
            <el-input v-model="newTripForm.tripBudget" type="number" />
          </el-form-item>
          <el-form-item label="Currency" prop="tripBudgetCurrency">
            <el-select v-model="newTripForm.tripBudgetCurrency" placeholder="Select">
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
          <el-form-item label="Remark" prop="remark">
            <el-input
                v-model="newTripForm.remark"
                type="textarea"
                rows="3"
                placeholder="Enter remarks if any"
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
                Click here to upload<br> an impressive image
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
                v-if="isEditing && newTripForm.imgPath && !selectedFile"
                :src="getFullImageUrl(newTripForm.imgPath)"
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
          <el-button @click="cancel">Cancel</el-button>
          <el-button type="primary" @click="submitTrip">Submit</el-button>
        </span>
        </template>
      </el-dialog>

      <!-- Add an action 新增行动 -->
      <el-dialog v-model="newActionDialogVisible" :title="'New Action of ' + currentTripName" width="500px">
        <el-form ref="actionFormRef" :rules="newActionRules" :model="newActionForm" label-width="120px">
          <el-form-item label="Name" prop="actionName">
            <el-input v-model="newActionForm.actionName" />
          </el-form-item>
          <el-form-item label="Type" prop="actionType">
            <el-select v-model="newActionForm.actionType" placeholder="Select">
              <el-option label="Transportation" value="Transportation" />
              <el-option label="Sightseeing" value="Sightseeing" />
              <el-option label="Rest" value="Rest" />
              <el-option label="Accommodation" value="Accommodation" />
              <el-option label="Meal" value="Meal" />
              <el-option label="Other" value="Other" />
            </el-select>
          </el-form-item>
          <el-form-item label="Start Time" prop="actionStartTime">
            <el-date-picker v-model="newActionForm.actionStartTime" type="datetime" placeholder="Pick the date and the time" />
          </el-form-item>
          <el-form-item label="End Time" prop="actionEndTime">
            <el-date-picker v-model="newActionForm.actionEndTime" type="datetime" placeholder="Pick the date and the time" />
          </el-form-item>
          <el-form-item label="Fare" prop="fare">
            <el-input-number
                v-model="newActionForm.fare"
                :min="0"
                :step="0.1"
                controls-position="right"
            />
          </el-form-item>
          <el-form-item label="Fare Currency" prop="fareCurrency">
            <el-select v-model="newActionForm.fareCurrency" placeholder="Select">
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
            <el-input v-model="newActionForm.description" />
          </el-form-item>
          <el-form-item label="Remark" prop="remark">
            <el-input
                v-model="newActionForm.remark"
                type="textarea"
                rows="3"
                placeholder="Enter remarks if any"
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
                Click here to upload<br> an impressive image
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
          </el-form-item>
          <el-form-item>
            <div slot="tip" class="el-upload__tip" style="color: red">
              Just click the button "Upload Image" to upload a new image.
            </div>
          </el-form-item>
        </el-form>
        <template #footer>
        <span class="dialog-footer">
          <el-button @click="actionCancel">Cancel</el-button>
          <el-button type="primary" @click="submitNewAction">Submit</el-button>
        </span>
        </template>
      </el-dialog>

      <!-- Remark popup 备注弹窗 -->
      <el-dialog v-model="remarkDialogVisible" :title="'Remark of ' + currentTripName" width="500px">
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
import {getMyTrips, deleteTripById, addTrip, editTrip, editTripWithImage, addTripWithImage} from "@/api/trip";
import {getFullImageUrl, formatDateWithoutTimezone, formatDateTimeWithoutTimezone} from "@/utils/utils";
import { computed } from 'vue';
import {addAction, addActionWithImage} from "@/api/action";

const loading = ref(false);
const router = useRouter();

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
const filteredTrips = computed(() => {
  if (!searchQuery.value) {
    return trips.value;
  }
  return trips.value.filter(trip =>
      trip.tripName.toLowerCase().includes(searchQuery.value.toLowerCase())
  );
});
// Pagination data, computed property 分页数据，计算属性
const paginatedTrips = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value;
  const end = start + pageSize.value;
  return filteredTrips.value.slice(start, end);
});

// Submit the form 提交表单
const formRef = ref();
// Travel list data, initially an empty array 旅行列表数据，初始为空数组
const trips = ref([]);
// Control the display of the "Add New Travel" dialog 控制新增旅行对话框的显示
const dialogVisible = ref(false);
// Mark whether it is edit mode 标记是否是编辑模式
const isEditing = ref(false);

// Form data for adding or editing a trip 新增或编辑旅行的表单数据
const newTripForm = ref({
  tripName: '',
  tripStartDate: null,
  tripEndDate: null,
  tripFrom: '',
  tripTo: '',
  tripBudget: null,
  tripBudgetCurrency: '',
  imgPath: '',
  remark: ''
});

// Form validation 表单校验
const rules = {
  // trigger: "blur" triggers on losing focus, while trigger: "change" triggers after a value change
  // trigger：blur是失焦时触发，change是修改后触发
  tripName: [
    { required: true, message: 'Please input the trip name', trigger: 'blur' }
  ],
  tripStartDate: [
    { required: true, message: 'Please select the start date', trigger: 'blur' }
  ],
  tripEndDate: [
    { required: true, message: 'Please select the end date', trigger: 'blur' }
  ],
  tripFrom: [
    { required: true, message: 'Please select the start location', trigger: 'blur' }
  ],
  tripTo: [
    { required: true, message: 'Please select the destination', trigger: 'blur' }
  ],
  tripBudget: [
    { required: true, message: 'Please input the budget', trigger: 'blur'}
  ],
  tripBudgetCurrency: [
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
  currentTripName.value = row.tripName;
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
  newTripForm.value = {
    tripName: '',
    tripStartDate: null,
    tripEndDate: null,
    tripFrom: '',
    tripTo: '',
    tripBudget: null,
    tripBudgetCurrency: '',
    imgPath: '',
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

// Get the trip list 获取旅行列表
const fetchTripList = async () => {
  loading.value = true;
  try {
    const response = await getMyTrips();
    trips.value = response.data.data;
  } catch (error) {
    console.error('Retrieve trips failed! ', error);
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
  newTripForm.value = { ...row };
  dialogVisible.value = true;
};

// Submit to add or edit a new trip 提交新增或编辑新的旅行
const submitTrip = async () => {
  formRef.value.validate(async (valid) => {
    if (!valid) {
      ElMessage.error('Please fill in the required fields.')
      return
    }
    // Submit 提交
    try {
      // Fix the time 固化时间
      const rightTimeTripForm = {
        ...newTripForm.value,
        tripStartDate: formatDateWithoutTimezone(newTripForm.value.tripStartDate),
        tripEndDate: formatDateWithoutTimezone(newTripForm.value.tripEndDate),
      };
      if (isEditing.value) {
        // The file has been uploaded 上传了文件
        if (selectedFile.value) {
          await editTripWithImage(rightTimeTripForm, selectedFile.value)
        } else {
          await editTrip(rightTimeTripForm)
        }
        ElMessage.success("Edited successfully!")
      } else {
        if (selectedFile.value) {
          await addTripWithImage(rightTimeTripForm, selectedFile.value);
        } else {
          await addTrip(rightTimeTripForm);
        }
        ElMessage.success("Trip added successfully!")
      }
      dialogVisible.value = false;
      // Reload the trip list 重新加载旅行列表
      await fetchTripList();
      resetForm();
    } catch (error) {
      ElMessage.error('Failed to submit the trip! ', error.message);
    }
  })
};

// Delete the trip 删除旅行
const deleteTrip = async (row) => {
  try {
    await ElMessageBox.confirm(
        `Do you want to delete the trip "${row.tripName}"?`,
        'Warning',
        {
          confirmButtonText: 'OK',
          cancelButtonText: 'CANCEL',
          type: 'warning',
        }
    );
    // The user clicked confirm, execute the delete logic 用户点击了确认，执行删除逻辑
    await deleteTripById(row.id);
    ElMessage.success('Trip deleted successfully!');
    // Reload the trip list 重新加载旅行列表
    await fetchTripList();
  } catch (error) {
    // The user clicked cancel or another error occurred 用户点击了取消或发生了其他错误
    if (error === 'cancel') {
      ElMessage.info('Trip delete cancelled!');
    } else {
      console.error('Trip delete failed! ', error);
    }
  }
};

// Navigate to the action detail page of the corresponding trip 跳转到对应trip的action详情页
const openActionDetailDialog = (row) => {
  router.push({
    path: '/action',
    query: { tripId: row.id }  // Include the tripId parameter 携带tripId参数
  });
};

// Lifecycle hook, executed after the component is mounted 生命周期钩子，在组件挂载后执行
onMounted(() => {
  fetchTripList();
});

/* Below is the code for the "Add Action" part 以下为新增行动部分代码 */
// Save the current row’s id as tripId 保存当行id作为tripId
const tripId = ref(null);
const currentTripName = ref('');

// New action form data 新增行动表单数据
const newActionForm = ref({
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

// Submit the action form 提交行动表单
const actionFormRef = ref();
// Control the visibility of the add action dialog 控制新增行动对话框的显示
const newActionDialogVisible = ref(false);

// Action form validation 行动表单校验
const newActionRules = {
  actionName: [
    { required: true, message: 'Please input the action name', trigger: 'blur' }
  ],
  actionType: [
    { required: true, message: 'Please select the action type', trigger: 'blur' }
  ],
  actionStartTime: [
    { required: true, message: 'Please select the start time', trigger: 'blur' }
  ],
  actionEndTime: [
    { required: true, message: 'Please select the end time', trigger: 'blur' }
  ],
  fare: [
      { required: true, message: 'Please input the fare', trigger: 'blur' }],
  fareCurrency: [
      { required: true, message: 'Please select the fare Currency', trigger: 'blur' }]
}

// Function to clear the action form 清空行动表单函数
const resetActionForm = () => {
  newActionForm.value = {
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
  // Clear all variables related to files 清空所有与文件相关的变量
  previewUrl.value = null;
  selectedFile.value = null;
  selectedFileName.value = null;
  fileList.value = [];
  // Clear the tripId 清空tripId
  tripId.value = null;
  currentTripName.value = '';
}

// Cancel button of new action 新增行动退出按钮
const actionCancel = () => {
  newActionDialogVisible.value = false;
  resetActionForm();
}

// Open the add action dialog 打开新增行动对话框
const openAddActionDialog = (row) => {
  newActionDialogVisible.value = true;
  // Assign the current row's id to tripId 给tripId赋值当行id
  tripId.value = row.id;
  currentTripName.value = row.tripName;
}

// Submit the new action 提交新的行动
const submitNewAction = async() => {
  actionFormRef.value.validate(async (valid) => {
    if (!valid) {
      ElMessage.error('Please fill in all supposed items!');
      return;
    }
    try {
      // Fix the time 固化时间
      const rightTimeActionForm = {
        ...newActionForm.value,
        actionStartTime: formatDateTimeWithoutTimezone(newActionForm.value.actionStartTime),
        actionEndTime: formatDateTimeWithoutTimezone(newActionForm.value.actionEndTime),
      };
      if (selectedFile.value) {
        await addActionWithImage(tripId.value, rightTimeActionForm, selectedFile.value);
      } else {
        await addAction(tripId.value, rightTimeActionForm);
      }
      ElMessage.success("New action created successfully!");
      newActionDialogVisible.value = false;
      // Reload the trip list 重新加载旅行列表
      await fetchTripList();
      // Clear the action form 清空action表单
      resetActionForm();
    } catch (error) {
      ElMessage.error('Failed to submit the new action! ', error.message);
    }
  })
}

</script>

<style scoped>
.trip-management {
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