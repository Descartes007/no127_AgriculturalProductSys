<template>
  <div>
    <div style="width: 60%; margin: 30px auto">
      <el-button
        type="success"

        style="font-size: 20px;"

        @click="saveIcon"
      >
        新增上级
      </el-button>
      <el-table :data="icons" stripe>
        <!-- 下级分类表-->
        <el-table-column type="expand" label="下级分类" width="100px">
          <template slot-scope="scope">
            <el-table
              :data="scope.row.categories"
              :header-cell-style="{ background: '#cbefea', color: 'black' }"
            >
              <el-table-column label="分类id" prop="id"></el-table-column>
              <el-table-column label="分类名称" prop="name"></el-table-column>
              <el-table-column label="操作" fixed="right">
                <template slot-scope="scope">
                  <el-button
                    type="primary"
                      size="mini"
                    @click="handleEditCategory(scope.row)"
                    >修改</el-button
                  >

                  <el-popconfirm
                    @confirm="deleteCategory(scope.row)"
                    title="确定删除？"
                  >
                    <el-button
                      type="danger"
                      size="mini"
                      slot="reference"
                      >删除</el-button
                    >
                  </el-popconfirm>
                </template>
              </el-table-column>
            </el-table>
          </template>
          <!---->
        </el-table-column>
        <el-table-column label="id" prop="id" ></el-table-column>


        <el-table-column fixed="right" label="操作" >
          <template slot-scope="scope">

            <el-button
              type="success"
              style="font-size: 20px;"
              @click="handleAddCategory(scope.row)"
            >
            新增下级
            </el-button>

            <el-popconfirm
              @confirm="deleteIcon(scope.row.id)"
              title="确定删除？"
            >
              <el-button
                type="danger"
                icon="iconfont icon-r-delete"
                style="font-size: 18px;margin-left: 10px;"

                slot="reference"
              >
                删除
              </el-button>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
    </div>

   <!--新增下级分类-->
    <el-dialog title="新增下级分类" :visible.sync="addSubFormVisible">


      <el-input v-model="addValue"></el-input>
      <div slot="footer" class="dialog-footer">
        <el-button @click="addSubFormVisible = false" style="font-size: 20px;"> 取消</el-button>
        <el-button type="primary" @click="addSubForm" style="font-size: 20px;"> 确定</el-button>
      </div>
    </el-dialog>

    <!--修改下级分类-->
    <el-dialog title="修改下级分类" :visible.sync="editSubFormVisible">
      <el-input v-model="editCategory.name"></el-input>
      <div slot="footer" class="dialog-footer">
        <el-button @click="editSubFormVisible = false" style="font-size: 20px;"> 取消</el-button>
        <el-button type="primary" @click="editSubForm" style="font-size: 20px;"> 确定</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>

export default {
  name: "Category",
  data() {
    return {
      options: [],
      searchText: "",
      user: {},
      icons: [],
      icon: {},
      addIcon: {},
      pageNum: 1,
      pageSize: 5,
      entity: {},
      total: 0,
      addSubFormVisible: false,
      editSubFormVisible: false,
      editCategory:{},
      addIconId:0,
      addValue:""
    };
  },
  created() {
    this.user = localStorage.getItem("user")
      ? JSON.parse(sessionStorage.getItem("user"))
      : {};
    this.load();

  },
  methods: {
    load() {
      this.request.get("/api/icon").then((res) => {
        this.icons = res.data;
      });
    },
    // 修改子集
    editSubForm(){
      console.log(this.editCategory,'editCategory')
      this.request.post("/api/category", this.editCategory).then((res) => {
        if (res.code === "200") {
          this.$message.success("修改成功");
        } else {
          this.$message.error("修改失败");
        }

        this.editSubFormVisible = false;
      });

    },

    // 添加子集
    addSubForm(){
        this.request
          .post("/api/category/add", { name: this.addValue, iconId: this.addIconId })
          .then((res) => {
            if (res.code === "200") {
              this.$message.success("新增成功");
              this.load();
            } else {
              this.$message.error("新增失败");
            }
            this.addSubFormVisible = false
          });
    },

    handleEditCategory(category) {
      this.editSubFormVisible =true;
      console.log(category,'category')
      this.editCategory =category;
    },
    handleAddCategory(icon) {
      this.addSubFormVisible =true
      this.addIconId = icon.id


      // this.$prompt("请输入新增的下级分类名称", "提示", {
      //   confirmButtonText: "确定",
      //   cancelButtonText: "取消",
      // }).then(({ value }) => {
      //
      //   if(value == null || value == undefined || value.trim() == "") {
      //       this.$message.error("请输入名称");
      //       return
      //   }
      //   this.request
      //     .post("/api/category/add", { name: value, iconId: icon.id })
      //     .then((res) => {
      //       if (res.code === "200") {
      //         this.$message.success("新增成功");
      //         this.load();
      //       } else {
      //         this.$message.error("新增失败");
      //       }
      //     });
      // });
    },
    handleEditIcon(icon) {
      this.icon = JSON.parse(JSON.stringify(icon));
      this.dialogFormVisible = true;
    },
    editIcon() {
      //删除无用的属性
      delete this.icon.categories;
      this.request.post("/api/icon", this.icon).then((res) => {
        if (res.code === "200") {
          this.$message.success("修改成功");
          this.dialogFormVisible = false;
        } else {
          this.$message.error("修改失败");
        }
      });
    },
    saveIcon() {
      this.addIcon.value = "&#xe606"
      // 新增上级分类
      if (this.addIcon.value == undefined) {
        this.$message.error("请选择上级分类图标");
        return;
      }
      this.request.post("/api/icon", this.addIcon).then((res) => {
        console.log(res);
        if (res.code === "200") {
          this.$message.success("新增成功");
          this.addDialogFormVisible = false;
          this.load();
        } else {
          this.$message.error("新增失败");
        }
      });
    },
    deleteIcon(iconId) {
      // 删除上级分类
      this.request.get("/api/icon/delete?id=" + iconId).then((res) => {
        if (res.code == "200") {
          this.$message.success("删除成功");
          this.load();
        } else {
          this.$message.error(res.msg);
        }
      });
    },
    deleteCategory(category) {
      // 删除下级分类
      this.request.get("/api/category/delete?id=" + category.id).then((res) => {
        if (res.code == "200") {
          this.$message.success("删除成功");
          this.load();
        } else {
          this.$message.error(res.msg);
        }
      });
    },
  },
};
</script>

<style scoped>
</style>
