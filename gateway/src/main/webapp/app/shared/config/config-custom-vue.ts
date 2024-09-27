import LabelComponent from '@/components/label/label.vue';
import ButtonComponent from '@/components/button/button.vue';

export function initCustomVue(vue) {
  vue.component('daic-label', LabelComponent);
  vue.component('daic-button', ButtonComponent);
}
