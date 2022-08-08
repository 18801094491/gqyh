<!-- 个人管理-通讯录 -->
<template>
  <div class="margin12">
    <a-row type="flex" :gutter="16">
      <a-col :md="5" :sm="24">
        <address-list-left v-model="currentOrgCode" />
      </a-col>
      <a-col :md="24 - 5" :sm="24">
        <address-list-right v-model="currentOrgCode" />
      </a-col>
    </a-row>
  </div>
</template>

<script>
import AddressListLeft from "./modules/AddressListLeft";
import AddressListRight from "./modules/AddressListRight";

export default {
  name: "AddressList",
  components: { AddressListLeft, AddressListRight },
  data() {
    return {
      description: "通讯录页面",
      currentOrgCode: "",
    };
  },

  methods: {},
};
</script>
<style scoped>
@import "~@assets/less/common.less";
</style>