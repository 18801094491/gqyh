<template>
    <div class="user-wrapper headerRightBox" :class="theme">
        <!-- <a @click="handleExportXls2('系统手册')"  download="系统手册">
          <a-icon type="question-circle" style="font-size:18px;cursor: pointer; color:#fff" title="系统手册"/>
        </a> -->
        <a-dropdown>
            <a class="action action2" style="color:#fff">
                <a-icon type="question-circle"
                        style="font-size:18px;cursor: pointer; color:#fff; margin-right:5px;position: relative;top:2px;"/>
                帮助中心
            </a>
            <a-menu slot="overlay" class="user-dropdown-menu-wrapper docBox">

                <a-menu-item v-for="(item,index) in docList" :key="index">
                    <a target="_black" :href="item.docUrl" :download="item.docName">

                        <a-icon type="question-circle"/>
                        {{item.docName}}
                    </a>
                    <!-- <router-link :to="{ name: 'account-settings-base' }">
                      <a-icon type="question-circle"/>
                      <span>系统手册</span>
                    </router-link> -->
                </a-menu-item>

            </a-menu>
        </a-dropdown>

        <header-notice class="action action2" style="top:0;"/>
        <a-dropdown>
             <!-- <a class="action action-full ant-dropdown-link user-dropdown-menu" @click="e => e.preventDefault()">
                 <a-avatar class="avatar" size="small" :src="getAvatar()"/>
                <span v-if="isDesktop()">欢迎您，{{ nickname() }}</span>
                <a-icon type="down" />
            </a> -->
            <span class="action action-full ant-dropdown-link user-dropdown-menu">
                <a-avatar class="avatar" size="small" :src="getAvatar()"/>
                <span v-if="isDesktop()">欢迎您，{{ nickname() }}</span>
                <a-icon type="down" />
            </span>
            <a-menu slot="overlay" class="user-dropdown-menu-wrapper">
                <a-menu-item key="0">
                    <router-link :to="{ name: 'account-settings-base' }">
                        <a-icon type="user"/>
                        <span>个人中心</span>
                    </router-link>
                </a-menu-item>

                <a-menu-item key="4" @click="updatePassword">
                    <a-icon type="setting"/>
                    <span>密码修改</span>
                </a-menu-item>
                <a-menu-item key="5" @click="updateCurrentDepart">
                    <a-icon type="cluster"/>
                    <span>切换部门</span>
                </a-menu-item>
                <a-menu-item>
                    <span class="action action2" @click="handleLogout">
                        <a class="logout_title" href="javascript:;">
                            <a-icon type="logout"/>
                            <span v-if="isDesktop()">&nbsp;退出登录</span>
                        </a>
                    </span>
                </a-menu-item>

            </a-menu>
        </a-dropdown>
        <!-- <span class="action action2" @click="handleLogout">
            <a class="logout_title" href="javascript:;">
                <a-icon type="logout"/>
                <span v-if="isDesktop()">&nbsp;退出登录</span>
            </a>
        </span> -->
        <user-password ref="userPassword"></user-password>
        <depart-select ref="departSelect" :closable="true" title="部门切换"></depart-select>
        <setting-drawer ref="settingDrawer" :closable="true" title="系统设置"></setting-drawer>
    </div>
</template>

<script>
    import index from '@assets/js/system/UserMenu.js';

    export default {
        ...index
    }
</script>

<style scoped>
    .logout_title {
        color: inherit;
        text-decoration: none;
    }

    .layout .header .headerRightBox .action2 {
        height: 100%;
        position: relative;
        top: -2px;
    }

    .docBox.user-dropdown-menu-wrapper.ant-dropdown-menu .ant-dropdown-menu-item {
        width: 100%;
    }
</style>