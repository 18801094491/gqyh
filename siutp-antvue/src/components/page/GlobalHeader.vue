<template>
    <!-- , width: fixedHeader ? `calc(100% - ${sidebarOpened ? 256 : 80}px)` : '100%'  -->
    <a-layout-header
            v-if="!headerBarFixed"
            :class="[fixedHeader && 'ant-header-fixedHeader', sidebarOpened ? 'ant-header-side-opened' : 'ant-header-side-closed', ]"
            :style="{ padding: '0' }">

        <div v-if="mode === 'sidemenu'" class="header" :class="theme">
            <a-icon
                    v-if="device==='mobile'"
                    class="trigger"
                    :type="collapsed ? 'menu-fold' : 'menu-unfold'"
                    @click.native="toggle('1')"></a-icon>
            <a-icon
                    v-else
                    class="trigger"
                    :type="collapsed ? 'menu-unfold' : 'menu-fold'"
                    @click.native="toggle('2')"/>

            <span v-if="device === 'desktop'" style="font-size:16px; display: inline-block; ">欢迎使用：<strong>{{systemName}}</strong></span>
            <span v-else style="font-size:16px; display: inline-block; ">欢迎使用：<strong>{{systemName}}</strong></span>
            <!-- <span v-else>DSC</span> -->

            <user-menu :theme="theme"/>
        </div>
        <!-- 顶部导航栏模式 -->
        <div v-else :class="['top-nav-header-index', theme]">
            <div class="header-index-wide">
                <div class="header-index-left" :style="topMenuStyle.headerIndexLeft">
                    <logo class="top-nav-header" :show-logoImg="headerBarFixed" :show-title="device !== 'mobile'"
                          :style="topMenuStyle.topNavHeader"/>
                    <div v-if="device !== 'mobile'" :style="topMenuStyle.topSmenuStyle">
                        <s-menu
                                mode="horizontal"
                                :menu="menus"
                                :theme="theme"></s-menu>
                    </div>
                    <a-icon
                            v-else
                            class="trigger"
                            :type="collapsed ? 'menu-fold' : 'menu-unfold'"
                            @click.native="toggle('3')"></a-icon>
                </div>
                <user-menu class="header-index-right" :theme="theme" :style="topMenuStyle.headerIndexRight"/>
            </div>
        </div>

    </a-layout-header>
</template>

<script>
    import index from '@assets/js/system/GlobalHeader.js'

    export default {
        ...index
    }
</script>

<style lang="scss" scoped>
    /* update_begin author:scott date:20190220 for: 缩小首页布局顶部的高度*/

    $height: 50px;

    .layout {

        .top-nav-header-index {

            .header-index-wide {
                margin-left: 10px;

                .ant-menu.ant-menu-horizontal {
                    height: $height;
                    line-height: $height;
                }
            }

            .trigger {
                line-height: 64px;

                &:hover {
                    background: rgba(0, 0, 0, 0.05);
                }
            }
        }

        .header {
            z-index: 2;
            color: white;
            height: 50px;
            // background-color: #1890ff;
            transition: background 300ms;
            background: url('~@/assets/logobg.png') repeat-x !important;
            /* dark 样式 */
            &.dark {
                color: #000000;
                box-shadow: 0 0 4px rgba(0, 0, 0, 0.2);
                background-color: white !important;
            }
        }

        .header, .top-nav-header-index {
            &.dark .trigger:hover {
                background: rgba(0, 0, 0, 0.05);
            }
        }
    }

    .ant-layout-header {
        height: $height;
        line-height: $height;
    }

    /* update_end author:scott date:20190220 for: 缩小首页布局顶部的高度*/

</style>