import LabelComponent from '@/components/label/label.vue';

export function initCustomVue(vue) {
  vue.component('d-label', LabelComponent);
}
