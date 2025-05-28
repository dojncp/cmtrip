<template>
  <main class="flex items-center justify-center min-h-screen bg-gradient-to-r from-blue-400 to-purple-500 p-4">
    <div class="bg-white p-8 rounded-2xl shadow-xl text-center max-w-2xl w-full mx-4 space-y-6">
      <h1 class="text-4xl font-bold text-gray-800">
        {{ $t('home.title') }}
      </h1>
      <p class="text-gray-600 text-base leading-relaxed">
        {{ $t('home.description') }}
      </p>
      <div class="flex flex-col sm:flex-row items-center justify-around gap-6 text-left">
        <div class="flex-1">
          <h2 class="mt-4 text-4xl font-bold">{{ $t('home.trip.title') }}</h2>
          <p class="text-sm mt-1 leading-relaxed">
            {{ $t('home.trip.description') }}
          </p>
        </div>
        <div class="flex-1">
          <h2 class="mt-4 text-4xl font-bold">{{ $t('home.action.title') }}</h2>
          <p class="text-sm mt-1 leading-relaxed">
            {{ $t('home.action.description') }}
          </p>
        </div>
      </div>
      <p class="text-gray-500 text-sm">
        {{ $t('home.photos') }}
      </p>
    </div>
  </main>
</template>

<script setup>
</script>

<style scoped>
/* Smooth scroll 平滑滚动 */
html {
  scroll-behavior: smooth;
}

/* Button press effect 按钮按下效果 */
button:active {
  transform: scale(0.98);
}

/* Responsive adjustment 响应式调整 */
@media (max-width: 640px) {
  .text-4xl {
    font-size: 1.75rem;
  }

  .p-8 {
    padding: 1.5rem;
  }
}
</style>
