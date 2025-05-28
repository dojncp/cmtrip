<template>
  <div class="language-switcher">
    <select v-model="locale">
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
.language-switcher select {
  padding: 6px 12px;
  border-radius: 4px;
  border: 1px solid #dcdfe6;
  background: white;
  cursor: pointer;
  transition: all 0.3s;
}

.language-switcher select:hover {
  border-color: #c0c4cc;
}
</style>