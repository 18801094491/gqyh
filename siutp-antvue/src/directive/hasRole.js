import { USER_ROLE } from "@/store/mutation-types"

const hasRole = {
  install (Vue, options) {
    Vue.directive('hasRole', {
      inserted: (el, binding, vnode)=>{
        filterUserRoles(el, binding, vnode);
      }
    });
  }
};

export function filterUserRoles(el, binding, vnode) {
  let roles = JSON.parse(sessionStorage.getItem(USER_ROLE) || "[]");
  if (roles === null || roles === "" || roles === undefined||roles.length<=0) {
    el.parentNode.removeChild(el);
    return;
  }
  if (!roles.includes(binding.value)) {
    el.parentNode.removeChild(el);
  }
}

export default hasRole;