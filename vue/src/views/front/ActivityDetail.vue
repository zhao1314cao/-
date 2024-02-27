<template>
  <div class="main-content">
    <div class="card" style="margin-bottom: 10px">
      <div style="display: flex; grid-gap: 20px">
        <img :src="activity.cover" alt="" style="width: 30%; height: 250px; border-radius: 5px">

        <div style="flex: 1; position:relative;">
          <div style="font-weight: bold; font-size: 24px; margin-bottom: 10px">{{ activity.name }}</div>
          <div style="color: #666; font-size: 13px; line-height: 22px; text-align: justify; margin-bottom: 10px">{{ activity.descr }}</div>
          <div style="color: #666; margin-bottom: 10px"><i class="el-icon-date"></i> 时间 <span style="margin-left: 5px">{{ activity.start }} ~ {{ activity.end }}</span></div>
          <div style="color: #666; margin-bottom: 10px"><i class="el-icon-location"></i> 地址
            <a style="color: #2a60c9; margin-left: 5px" :href="activity.address" v-if="activity.form === '线上'">{{ activity.address }}</a>
            <span style="margin-left: 5px" v-else>{{ activity.address }}</span>
            <el-tag style="margin-left: 5px" type="primary" size="mini">{{ activity.form }}</el-tag>
          </div>
          <div style="position: absolute;bottom: 0 ;width: 100%">
            <div style="display: flex; align-items: center">
              <div style="flex: 1">
                <el-button type="primary" disabled v-if="activity.isEnd" key="已结束">已结束</el-button>
                <el-button type="success" v-else-if="activity.isSign" :key="signText" @click="cancel" @mouseenter.native="signText='取消报名'" @mouseleave.native="signText='已报名'">
                  {{signText}}</el-button>
                <el-button type="primary" v-else @click="sign">报名</el-button>
              </div>

              <div style="text-align: right; flex: 1">
                <el-button type="danger" v-if="activity.isLike" @click="like">已点赞 {{ activity.likesCount }}</el-button>
                <el-button type="danger" v-else @click="like">点赞 {{ activity.likesCount }}</el-button>
                <el-button type="warning" v-if="activity.isCollect" @click="collect">已收藏 {{ activity.collectCount }}</el-button>
                <el-button type="warning" v-else @click="collect">收藏 {{ activity.collectCount }}</el-button>
                <el-button type="info">阅读量 {{ activity.readCount }}</el-button>
              </div>
            </div>
          </div>

        </div>
      </div>
    </div>

    <div class="card" style="margin-bottom: 10px">
      <div style="font-weight: bold; font-size: 24px; margin-bottom: 20px; padding-left: 10px; border-left: 5px solid #2a60c9">活动详情</div>
      <div class="w-e-text">
        <div v-html="activity.content"></div>
      </div>
    </div>

    <div>
      <Comment :fid="activityId" module="活动" />
    </div>

  </div>
</template>

<script>
import Comment from "@/components/Comment";

export default {
  name: "ActivityDetail",
  components: {Comment},
  data() {
    return {
      activity: {},
      activityId: this.$route.query.activityId,
      userId: null,
      time: null,
      signText: null
    }
  },
  created() {
    this.load()
    this.updateReadCount()
  },
  methods: {
    cancel() {   // 单个取消
      this.$confirm('您确定取消报名吗？', '确认取消', {type: "warning"}).then(response => {
        this.$request.delete('/activitySign/deleteByActivityId/' + this.activityId).then(res => {
          if (res.code === '200') {   // 表示操作成功
            this.$message.success('操作成功')
            this.load(1)
          } else {
            this.$message.error(res.msg)  // 弹出错误的信息
          }
        })
      }).catch(() => {
      })
    },
    like(){
      this.$request.post('/likes/set/',{ fid:this.activityId,module:'活动'}).then(res => {
        if(res.code==='200'){
          this.$message.success('点赞成功')
          this.load()
        }else{
          this.$message.error(res.msg)
        }
      })
    },
    collect(){
      this.$request.post('/collect/set/',{ fid:this.activityId,module:'活动'}).then(res => {
        if(res.code==='200'){
          this.$message.success('收藏成功')
          this.load()
        }else{
          this.$message.error(res.msg)
        }
      })
    },
    sign(){
      this.$request.post('/activitySign/add/',{ activityId:this.activityId}).then(res => {
         if(res.code==='200'){
           this.$message.success('报名成功')
           this.load()
         }else{
           this.$message.error(res.msg)
         }
      })
    },
    load() {
      this.$request.get('/activity/selectById/' + this.activityId).then(res => {
        this.activity = res.data || {}
      })
    },
    updateReadCount(){
      this.$request.put('/activity/updateReadCount/'+this.activityId)
    }
  }
}
</script>

<style scoped>

</style>