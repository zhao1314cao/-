<template>
  <div style="margin: 5px auto;width: 50%">
    <blog-list />
  </div>
</template>

<script>
import BlogList from "@/components/BlogList";

export default {
  name: "Search",
  components: {BlogList},
  data() {
    return {
      title: this.$route.query.title
    }
  },
  created() {

  },
  methods: {}
}
</script>

<style scoped>

</style>