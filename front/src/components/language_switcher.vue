<template>
  <div class="language-switcher">
    <select v-model="locale" class="styled-select">
      <option value="en">English</option>
      <option value="zh">中文</option>
      <option value="fr">Français</option>
      <option value="es">Español</option>
      <option value="ru">Русский</option>
      <option value="ar">العربية</option>
      <option value="jp">日本語</option>
    </select>
  </div>
</template>

<script setup>
import { useI18n } from 'vue-i18n'
const { locale } = useI18n({ useScope: 'global' })
</script>

<style scoped>
.language-switcher {
  display: inline-block;
  margin-left: 10px;
}

.styled-select {
  padding: 8px 15px;
  border-radius: 8px;
  border: none;
  background: linear-gradient(135deg, rgba(156, 191, 243, 0.8) 0%, rgba(114, 191, 252, 0.7) 100%);
  color: #2d3748;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.175, 0.885, 0.32, 1.1);
  font-size: 14px;
  font-weight: 500;
  appearance: none;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  outline: none;
  min-width: 120px;
  text-align: center;
}

.styled-select:hover {
  transform: translateY(-2px);
  box-shadow: 0 7px 14px rgba(0, 0, 0, 0.15);
  background: linear-gradient(135deg, rgba(156, 191, 243, 1) 0%, rgba(114, 191, 252, 0.9) 100%);
}

.styled-select:focus {
  box-shadow: 0 0 0 2px rgba(66, 153, 225, 0.5);
}

/* 下拉箭头样式 */
.styled-select {
  background-image:
      linear-gradient(45deg, transparent 50%, #2d3748 50%),
      linear-gradient(135deg, #2d3748 50%, transparent 50%);
  background-position:
      calc(100% - 15px) 50%,
      calc(100% - 10px) 50%;
  background-size:
      5px 5px,
      5px 5px;
  background-repeat: no-repeat;
}

/* 选项样式 */
.styled-select option {
  background: white;
  color: #2d3748;
  padding: 8px;
}
</style>