<template>
  <div class="page-container">
    <!-- Language switch -->
    <div class="language-switcher">
      <div class="container">
        <div class="lang-buttons">
          <button
              v-for="lang in supportedLanguages"
              :key="lang.code"
              :class="{ active: currentLanguage === lang.code }"
              @click="changeLanguage(lang.code)"
          >
            {{ lang.name }}
          </button>
        </div>
      </div>
    </div>

    <!-- Return button -->
    <div class="back-to-home">
      <router-link to="/home" class="btn btn-secondary">
        <i class="fas fa-home"></i>
        <span>{{ currentTranslations.backToHome }}</span>
      </router-link>
    </div>

    <div class="content-wrapper">
      <div class="page-header">
        <img src="/favicon.ico" alt="CMTRIP Icon" class="page-icon" />
        <h1 class="page-title">{{ currentTranslations.title }}</h1>
      </div>

      <div class="content-section">
        <h2>{{ currentTranslations.section1.title }}</h2>
        <p>{{ currentTranslations.section1.content }}</p>

        <h2>{{ currentTranslations.section2.title }}</h2>
        <p>{{ currentTranslations.section2.content }}</p>

        <h2>{{ currentTranslations.section3.title }}</h2>
        <p>
          {{ currentTranslations.section3.content1 }}
          <a href="/help">{{ currentTranslations.section3.link }}</a>.
          {{ currentTranslations.section3.content2 }}
        </p>

        <h2>{{ currentTranslations.section4.title }}</h2>
        <p>{{ currentTranslations.section4.content }}</p>

        <h2>{{ currentTranslations.section5.title }}</h2>
        <p>{{ currentTranslations.section5.content }}</p>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "PrivacyPolicies",
  data() {
    return {
      supportedLanguages: [
        { code: 'en', name: 'English' },
        { code: 'zh', name: '中文' },
        { code: 'fr', name: 'Français' },
        { code: 'es', name: 'Español' },
        { code: 'ru', name: 'Русский' },
        { code: 'ar', name: 'العربية' }
      ],
      currentLanguage: 'en',
      translations: {
        en: {
          backToHome: "Back to Home",
          title: "CMTRIP Privacy Policies",
          section1: {
            title: "1. Data Collection & Usage",
            content:
                "At CMTRIP, we respect your privacy and are committed to protecting your personal information. We collect data such as your name, email, and travel preferences to enhance your trip planning experience, including personalized destination recommendations and booking management.",
          },
          section2: {
            title: "2. Sharing Your Information",
            content:
                "We do not sell your data to third parties. However, we may share it with trusted travel partners (e.g., hotels, airlines) to fulfill your bookings. All partners are obligated to maintain data confidentiality in compliance with CMTRIP's standards.",
          },
          section3: {
            title: "3. Your Rights",
            content1: "You may request access to, correct, or delete your personal data at any time by contacting our ",
            link: "Support Team",
            content2: "We will respond to your request within 15 business days.",
          },
          section4: {
            title: "4. Data Security",
            content:
                "CMTRIP employs industry-standard security measures, including SSL encryption and regular audits, to protect your data from unauthorized access.",
          },
          section5: {
            title: "5. Policy Updates",
            content:
                "This policy may be updated periodically. We will notify you of significant changes via email or in-app notifications.",
          },
        },
        zh: {
          backToHome: "返回首页",
          title: "CMTRIP 隐私政策",
          section1: {
            title: "1. 数据收集与使用",
            content:
                "在 CMTRIP，我们尊重您的隐私，并致力于保护您的个人信息。我们收集您的姓名、电子邮箱和出行偏好等信息，以提升您的旅行计划体验，包括个性化目的地推荐和预订管理。",
          },
          section2: {
            title: "2. 信息共享",
            content:
                "我们不会将您的数据出售给第三方，但可能会与值得信赖的旅行合作伙伴（如酒店、航空公司）共享，以完成您的预订。所有合作方都需遵守 CMTRIP 的数据保密标准。",
          },
          section3: {
            title: "3. 您的权利",
            content1: "您可以随时联系我们的 ",
            link: "客服团队",
            content2: "以访问、更正或删除您的个人数据。我们将在15个工作日内回应您的请求。",
          },
          section4: {
            title: "4. 数据安全",
            content:
                "CMTRIP 采用行业标准的安全措施，包括 SSL 加密和定期审计，以防止未经授权的访问。",
          },
          section5: {
            title: "5. 政策更新",
            content:
                "本政策可能会定期更新。如有重大变更，我们将通过电子邮件或应用内通知告知您。",
          },
        },
        fr: {
          backToHome: "Retour à l'accueil",
          title: "Politiques de confidentialité de CMTRIP",
          section1: {
            title: "1. Collecte et utilisation des données",
            content:
                "Chez CMTRIP, nous respectons votre vie privée. Nous recueillons des données comme votre nom, email et préférences de voyage pour améliorer votre expérience.",
          },
          section2: {
            title: "2. Partage de vos informations",
            content:
                "Nous ne vendons pas vos données. Elles peuvent être partagées avec nos partenaires de voyage pour vos réservations.",
          },
          section3: {
            title: "3. Vos droits",
            content1: "Vous pouvez contacter notre ",
            link: "équipe de support",
            content2: " pour accéder, corriger ou supprimer vos données.",
          },
          section4: {
            title: "4. Sécurité des données",
            content:
                "Nous utilisons des mesures de sécurité comme le cryptage SSL pour protéger vos données.",
          },
          section5: {
            title: "5. Mises à jour",
            content:
                "Nous vous informerons des modifications importantes par e-mail ou notifications.",
          },
        },
        es: {
          backToHome: "Volver al inicio",
          title: "Políticas de privacidad de CMTRIP",
          section1: {
            title: "1. Recopilación y uso de datos",
            content:
                "En CMTRIP, respetamos su privacidad. Recopilamos datos como su nombre, correo electrónico y preferencias de viaje.",
          },
          section2: {
            title: "2. Compartir su información",
            content:
                "No vendemos sus datos. Podemos compartirlos con socios de confianza para completar sus reservas.",
          },
          section3: {
            title: "3. Sus derechos",
            content1: "Puede contactar con nuestro ",
            link: "equipo de soporte",
            content2: " para acceder, corregir o eliminar sus datos.",
          },
          section4: {
            title: "4. Seguridad de los datos",
            content:
                "Usamos medidas de seguridad estándar como cifrado SSL para proteger sus datos.",
          },
          section5: {
            title: "5. Actualizaciones de la política",
            content:
                "Le informaremos sobre cambios importantes por correo electrónico o notificaciones.",
          },
        },
        ru: {
          backToHome: "Вернуться на главную",
          title: "Политика конфиденциальности CMTRIP",
          section1: {
            title: "1. Сбор и использование данных",
            content:
                "В CMTRIP мы уважаем вашу конфиденциальность. Мы собираем имя, почту и предпочтения для планирования поездок.",
          },
          section2: {
            title: "2. Передача информации",
            content:
                "Мы не продаем ваши данные. Они могут передаваться надежным партнёрам для выполнения бронирований.",
          },
          section3: {
            title: "3. Ваши права",
            content1: "Вы можете обратиться в нашу ",
            link: "службу поддержки",
            content2: " для доступа, исправления или удаления данных.",
          },
          section4: {
            title: "4. Безопасность данных",
            content:
                "Мы используем шифрование SSL и регулярные проверки для защиты ваших данных.",
          },
          section5: {
            title: "5. Обновления политики",
            content:
                "Мы будем уведомлять вас о важных изменениях по почте или в приложении.",
          },
        },
        ar: {
          backToHome: "العودة إلى الصفحة الرئيسية",
          title: "سياسات الخصوصية لـ CMTRIP",
          section1: {
            title: "١. جمع البيانات واستخدامها",
            content:
                "في CMTRIP، نحترم خصوصيتك وملتزمون بحماية معلوماتك الشخصية. نقوم بجمع بيانات مثل الاسم والبريد الإلكتروني وتفضيلات السفر لتحسين تجربتك في التخطيط للسفر، بما في ذلك التوصيات المخصصة وإدارة الحجوزات.",
          },
          section2: {
            title: "٢. مشاركة معلوماتك",
            content:
                "نحن لا نبيع بياناتك لأطراف ثالثة. ومع ذلك، قد نشاركها مع شركاء سفر موثوقين (مثل الفنادق وشركات الطيران) لتنفيذ الحجوزات. جميع الشركاء ملزمون بالحفاظ على سرية البيانات وفقًا لمعايير CMTRIP.",
          },
          section3: {
            title: "٣. حقوقك",
            content1: "يمكنك طلب الوصول إلى بياناتك الشخصية أو تصحيحها أو حذفها في أي وقت من خلال التواصل مع ",
            link: "فريق الدعم",
            content2: "سنرد على طلبك خلال 15 يوم عمل.",
          },
          section4: {
            title: "٤. أمان البيانات",
            content:
                "تستخدم CMTRIP إجراءات أمان قياسية في هذا المجال، بما في ذلك تشفير SSL والمراجعات الدورية، لحماية بياناتك من الوصول غير المصرح به.",
          },
          section5: {
            title: "٥. تحديثات السياسة",
            content:
                "قد يتم تحديث هذه السياسة بشكل دوري. سنخطرك بالتغييرات الجوهرية عبر البريد الإلكتروني أو الإشعارات داخل التطبيق.",
          },
        },
      },
    };
  },
  computed: {
    currentTranslations() {
      return this.translations[this.currentLanguage] || this.translations.en;
    }
  },
  created() {
    const savedLang = localStorage.getItem('appLanguage');
    if (savedLang && this.supportedLanguages.some(l => l.code === savedLang)) {
      this.currentLanguage = savedLang;
    }
  },
  methods: {
    changeLanguage(lang) {
      this.currentLanguage = lang;
      localStorage.setItem('appLanguage', lang);
    }
  }
};
</script>

<style scoped lang="scss">
.page-container {
  min-height: 100vh;
  line-height: 1.8;
  position: relative;
  background: linear-gradient(135deg, #f5f7fa 0%, #e4ebf5 100%);
  padding-top: 50px;
}

// Language switch pattern
.language-switcher {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  background-color: #fff;
  box-shadow: 0 2px 5px rgba(0,0,0,0.1);
  z-index: 1001;
  padding: 10px 0;

  .container {
    max-width: 1200px;
    margin: 0 auto;
    display: flex;
    justify-content: center;
  }

  .lang-buttons {
    display: flex;
    gap: 10px;

    button {
      padding: 6px 12px;
      border: none;
      border-radius: 4px;
      background-color: #f0f2f5;
      cursor: pointer;
      transition: all 0.3s;
      font-size: 0.9rem;

      &:hover {
        background-color: #e2e8f0;
      }

      &.active {
        background-color: #667eea;
        color: white;
        font-weight: 500;
      }
    }
  }
}

.back-to-home {
  position: fixed;
  top: 70px;
  left: 20px;
  z-index: 1000;

  .btn-secondary {
    padding: 10px 20px;
    font-size: 14px;
    display: flex;
    align-items: center;
    gap: 6px;
    background: rgba(255, 255, 255, 0.9);
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
    transition: all 0.3s ease;

    &:hover {
      background: white;
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
      transform: translateX(-2px);
    }

    .fas.fa-home {
      font-size: 1.2em;
      color: #667eea;
    }
  }
}

.content-wrapper {
  max-width: 1200px;
  margin: 0 auto;
  padding: 80px 40px 40px;
}

.page-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 30px;

  .page-icon {
    width: 2.2em;
    filter: invert(0.3) sepia(1) hue-rotate(180deg) saturate(2);
  }

  .page-title {
    font-size: 2.4rem;
    color: #2d3748;
    border-bottom: 3px solid #4299e1;
    padding-bottom: 8px;
  }
}

.content-section {
  background: white;
  padding: 40px;
  border-radius: 16px;
  box-shadow: 0 15px 30px rgba(0, 0, 0, 0.05);

  h2 {
    color: #4a5568;
    font-size: 1.5rem;
    margin-top: 35px;
  }

  p {
    color: #5a677b;
    font-size: 1.05rem;
    line-height: 1.7;
  }
}
</style>