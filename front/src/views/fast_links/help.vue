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
        <span>{{ currentTranslations.backHome }}</span>
      </router-link>
    </div>

    <div class="content-wrapper">
      <div class="page-header">
        <img src="/favicon.ico" alt="CMTRIP Icon" class="page-icon" />
        <h1 class="page-title">{{ currentTranslations.pageTitle }}</h1>
      </div>

      <div class="faq-section">
        <h2>{{ currentTranslations.commonQuestions }}</h2>
        <div v-for="faq in translatedFaqList" :key="faq.id" class="faq-item">
          <h3>{{ faq.question }}</h3>
          <p>{{ faq.answer }}</p>
        </div>

        <h2>{{ currentTranslations.contactSupport }}</h2>
        <div class="contact-info">
          <p>
            <strong>{{ currentTranslations.email }}:</strong>
            <a href="mailto:dojncp@outlook.com">dojncp@outlook.com</a><br />
            <strong>{{ currentTranslations.hours }}:</strong>
            {{ currentTranslations.supportHours }}
          </p>
        </div>

        <h2>{{ currentTranslations.gettingStarted }}</h2>
        <div class="guide-steps">
          <div class="step">
            <h3>{{ currentTranslations.step1 }}</h3>
            <p>{{ currentTranslations.step1Desc }}</p>
          </div>
          <div class="step">
            <h3>{{ currentTranslations.step2 }}</h3>
            <p>{{ currentTranslations.step2Desc }}</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "HelpCenter",
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
          backHome: "Back to Home",
          pageTitle: "CMTRIP Help Center",
          commonQuestions: "Common Questions",
          contactSupport: "Contact CMTRIP Support",
          email: "Email",
          hours: "Hours",
          supportHours: "9:00 AM - 6:00 PM (UTC+8), Monday to Friday",
          gettingStarted: "Getting Started Guide",
          step1: "1. Create an Account",
          step1Desc: "Click \"Sign Up\" on the homepage and complete your profile with valid information.",
          step2: "2. Manage Your Trips",
          step2Desc: "Access your bookings in \"My Trips\" and make changes or view details as needed."
        },
        zh: {
          backHome: "返回主页",
          pageTitle: "CMTRIP帮助中心",
          commonQuestions: "常见问题",
          contactSupport: "联系CMTRIP客服",
          email: "邮箱",
          hours: "工作时间",
          supportHours: "上午9:00 - 下午6:00 (UTC+8)，周一至周五",
          gettingStarted: "入门指南",
          step1: "1. 创建账户",
          step1Desc: "点击首页的\"注册\"按钮，填写有效信息完成个人资料。",
          step2: "2. 管理您的行程",
          step2Desc: "在\"我的行程\"中查看您的预订，根据需要进行修改或查看详情。"
        },
        fr: {
          backHome: "Retour à l'accueil",
          pageTitle: "Centre d'aide CMTRIP",
          commonQuestions: "Questions fréquentes",
          contactSupport: "Contacter le support CMTRIP",
          email: "Email",
          hours: "Heures d'ouverture",
          supportHours: "9:00 AM - 6:00 PM (UTC+8), du lundi au vendredi",
          gettingStarted: "Guide de démarrage",
          step1: "1. Créer un compte",
          step1Desc: "Cliquez sur \"S'inscrire\" sur la page d'accueil et complétez votre profil avec des informations valides.",
          step2: "2. Gérer vos voyages",
          step2Desc: "Accédez à vos réservations dans \"Mes voyages\" et effectuez des modifications ou consultez les détails selon vos besoins."
        },
        es: {
          backHome: "Volver a Inicio",
          pageTitle: "Centro de Ayuda de CMTRIP",
          commonQuestions: "Preguntas Frecuentes",
          contactSupport: "Contactar con el Soporte de CMTRIP",
          email: "Correo electrónico",
          hours: "Horario",
          supportHours: "9:00 AM - 6:00 PM (UTC+8), de lunes a viernes",
          gettingStarted: "Guía de inicio",
          step1: "1. Crear una cuenta",
          step1Desc: "Haga clic en \"Registrarse\" en la página de inicio y complete su perfil con información válida.",
          step2: "2. Gestionar sus viajes",
          step2Desc: "Acceda a sus reservas en \"Mis viajes\" y realice cambios o consulte detalles según sea necesario."
        },
        ru: {
          backHome: "Вернуться на главную",
          pageTitle: "Центр поддержки CMTRIP",
          commonQuestions: "Частые вопросы",
          contactSupport: "Связаться с поддержкой CMTRIP",
          email: "Электронная почта",
          hours: "Часы работы",
          supportHours: "9:00 - 18:00 (UTC+8), с понедельника по пятницу",
          gettingStarted: "Руководство по началу работы",
          step1: "1. Создать учетную запись",
          step1Desc: "Нажмите \"Зарегистрироваться\" на главной странице и заполните профиль действительной информацией.",
          step2: "2. Управлять вашими поездками",
          step2Desc: "Перейдите в раздел «Мои поездки» и вносите изменения или просматривайте подробности по мере необходимости."
        },
        ar: {
          backHome: "العودة إلى الصفحة الرئيسية",
          pageTitle: "مركز مساعدة CMTRIP",
          commonQuestions: "الأسئلة الشائعة",
          contactSupport: "الاتصال بدعم CMTRIP",
          email: "البريد الإلكتروني",
          hours: "ساعات العمل",
          supportHours: "من 9:00 صباحًا إلى 6:00 مساءً (UTC+8)، من الاثنين إلى الجمعة",
          gettingStarted: "دليل البدء",
          step1: "1. إنشاء حساب",
          step1Desc: "انقر على \"تسجيل\" في الصفحة الرئيسية وأكمل ملفك الشخصي بمعلومات صحيحة.",
          step2: "2. إدارة رحلاتك",
          step2Desc: "ادخل إلى قسم \"رحلاتي\" لتعديل أو عرض تفاصيل حجوزاتك حسب الحاجة."
        }
      },
      faqList: [
        {
          id: 1,
          question: {
            en: "How do I reset my password?",
            zh: "如何重置我的密码？",
            fr: "Comment puis-je réinitialiser mon mot de passe ?",
            es: "¿Cómo restablezco mi contraseña?",
            ru: "Как сбросить пароль?",
            ar: "كيف يمكنني إعادة تعيين كلمة المرور الخاصة بي؟"
          },
          answer: {
            en: "Go to the login page and click 'Forgot Password.' Follow the instructions sent to your registered email.",
            zh: "访问登录页面，点击'忘记密码'。按照发送到您注册邮箱的说明操作。",
            fr: "Accédez à la page de connexion et cliquez sur 'Mot de passe oublié'. Suivez les instructions envoyées à votre adresse e-mail enregistrée.",
            es: "Vaya a la página de inicio de sesión y haga clic en 'Olvidé mi contraseña'. Siga las instrucciones enviadas a su correo electrónico registrado.",
            ru: "Перейдите на страницу входа и нажмите «Забыли пароль». Следуйте инструкциям, отправленным на вашу зарегистрированную электронную почту.",
            ar: "اذهب إلى صفحة تسجيل الدخول وانقر على 'نسيت كلمة المرور'. اتبع التعليمات المرسلة إلى بريدك الإلكتروني المسجل."
          }
        },
        {
          id: 2,
          question: {
            en: "Can I modify my travel dates?",
            zh: "我可以修改我的旅行日期吗？",
            fr: "Puis-je modifier mes dates de voyage ?",
            es: "¿Puedo modificar mis fechas de viaje?",
            ru: "Можно ли изменить даты поездки?",
            ar: "هل يمكنني تعديل تواريخ السفر الخاصة بي؟"
          },
          answer: {
            en: "Yes, most bookings can be modified. Visit 'My Trips' and select 'Modify Booking' to adjust your dates.",
            zh: "是的，大多数预订都可以修改。访问'我的行程'，选择'修改预订'来调整您的日期。",
            fr: "Oui, la plupart des réservations peuvent être modifiées. Visitez 'Mes voyages' et sélectionnez 'Modifier la réservation' pour ajuster vos dates.",
            es: "Sí, la mayoría de las reservas se pueden modificar. Visite 'Mis viajes' y seleccione 'Modificar reserva' para ajustar sus fechas.",
            ru: "Да, большинство бронирований можно изменить. Перейдите в раздел «Мои поездки» и выберите «Изменить бронирование», чтобы изменить даты.",
            ar: "نعم، يمكن تعديل معظم الحجوزات. انتقل إلى \"رحلاتي\" واختر \"تعديل الحجز\" لتغيير التواريخ."
          }
        }
      ]
    };
  },
  computed: {
    currentTranslations() {
      return this.translations[this.currentLanguage] || this.translations.en;
    },
    translatedFaqList() {
      return this.faqList.map(faq => ({
        id: faq.id,
        question: faq.question[this.currentLanguage],
        answer: faq.answer[this.currentLanguage]
      }));
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

.faq-section {
  background: white;
  padding: 40px;
  border-radius: 16px;
  box-shadow: 0 15px 30px rgba(0, 0, 0, 0.05);

  h2 {
    color: #4a5568;
    font-size: 1.5rem;
    margin-top: 35px;
  }

  .faq-item {
    margin: 25px 0;
    padding: 20px;
    border: 1px solid #e2e8f0;
    border-radius: 12px;
    background-color: #f8fafc;

    h3 {
      color: #2d3748;
      font-size: 1.15rem;
      margin-bottom: 12px;
    }

    p {
      color: #5a677b;
      font-size: 1.05rem;
    }
  }

  .contact-info {
    margin-top: 40px;
    padding: 25px;
    background: linear-gradient(135deg, rgba(102, 126, 234, 0.05) 0%, rgba(118, 75, 162, 0.05) 100%);
    border-radius: 12px;
  }

  .guide-steps {
    margin-top: 40px;

    .step {
      margin-bottom: 25px;
      padding: 20px;
      border-left: 4px solid #667eea;
      background-color: rgba(102, 126, 234, 0.03);
      border-radius: 4px;

      h3 {
        color: #2d3748;
        font-size: 1.2rem;
        margin-bottom: 10px;
      }

      p {
        color: #5a677b;
      }
    }
  }
}
</style>
