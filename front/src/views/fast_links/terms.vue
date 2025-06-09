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
        <p>{{ currentTranslations.section3.content }}</p>

        <h2>{{ currentTranslations.section4.title }}</h2>
        <p>{{ currentTranslations.section4.content }}</p>

        <h2>{{ currentTranslations.section5.title }}</h2>
        <p>{{ currentTranslations.section5.content }}</p>

        <h2>{{ currentTranslations.section6.title }}</h2>
        <p>{{ currentTranslations.section6.content }}</p>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "ServiceTerms",
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
          title: "CMTRIP Service Terms",
          section1: {
            title: "1. User Obligations",
            content: "By using CMTRIP, you agree to provide accurate registration information and comply with all platform rules. Unauthorized access, data scraping, or fraudulent bookings are strictly prohibited and may result in account suspension."
          },
          section2: {
            title: "2. Booking Terms",
            content: "All bookings are subject to the service provider's terms (e.g., cancellation fees, age restrictions). CMTRIP acts as an intermediary and is not liable for direct disputes between users and providers."
          },
          section3: {
            title: "3. Intellectual Property",
            content: "The CMTRIP logo, platform design, and content (e.g., destination guides) are protected by copyright. You may not reproduce or distribute any part of the platform without prior written consent."
          },
          section4: {
            title: "4. Governing Law",
            content: "These terms are governed by the laws of [Your Jurisdiction]. Disputes will be resolved through binding arbitration in [City]."
          },
          section5: {
            title: "5. Termination",
            content: "CMTRIP reserves the right to terminate your account at any time for violations of these terms or suspicious activity."
          },
          section6: {
            title: "6. Limitation of Liability",
            content: "CMTRIP is not responsible for any delays, cancellations, or disruptions caused by third-party service providers."
          }
        },
        zh: {
          backToHome: "返回首页",
          title: "CMTRIP服务条款",
          section1: {
            title: "1. 用户义务",
            content: "使用CMTRIP即表示您同意提供准确的注册信息并遵守所有平台规则。严禁未经授权的访问、数据抓取或欺诈性预订，违者可能导致账户暂停。"
          },
          section2: {
            title: "2. 预订条款",
            content: "所有预订均受服务提供商条款约束（如取消费用、年龄限制）。CMTRIP作为中介，不对用户与提供商之间的直接纠纷负责。"
          },
          section3: {
            title: "3. 知识产权",
            content: "CMTRIP标志、平台设计和内容（如目的地指南）受版权保护。未经事先书面同意，您不得复制或分发平台的任何部分。"
          },
          section4: {
            title: "4. 适用法律",
            content: "本条款受[您所在司法管辖区]法律管辖。争议将通过[城市]的具有约束力的仲裁解决。"
          },
          section5: {
            title: "5. 终止",
            content: "CMTRIP保留随时因违反本条款或可疑活动而终止您账户的权利。"
          },
          section6: {
            title: "6. 责任限制",
            content: "CMTRIP不对第三方服务提供商造成的任何延误、取消或中断负责。"
          }
        },
        fr: {
          backToHome: "Retour à l'accueil",
          title: "Conditions de service CMTRIP",
          section1: {
            title: "1. Obligations de l'utilisateur",
            content: "En utilisant CMTRIP, vous acceptez de fournir des informations d'inscription exactes et de respecter toutes les règles de la plateforme. L'accès non autorisé, le scraping de données ou les réservations frauduleuses sont strictement interdits et peuvent entraîner la suspension du compte."
          },
          section2: {
            title: "2. Conditions de réservation",
            content: "Toutes les réservations sont soumises aux conditions du prestataire de services (par exemple, frais d'annulation, restrictions d'âge). CMTRIP agit en tant qu'intermédiaire et n'est pas responsable des litiges directs entre les utilisateurs et les prestataires."
          },
          section3: {
            title: "3. Propriété intellectuelle",
            content: "Le logo CMTRIP, la conception de la plateforme et le contenu (par exemple, les guides de destination) sont protégés par des droits d'auteur. Vous ne pouvez pas reproduire ou distribuer une partie de la plateforme sans autorisation écrite préalable."
          },
          section4: {
            title: "4. Loi applicable",
            content: "Ces conditions sont régies par les lois de [votre juridiction]. Les litiges seront résolus par arbitrage contraignant à [ville]."
          },
          section5: {
            title: "5. Résiliation",
            content: "CMTRIP se réserve le droit de résilier votre compte à tout moment en cas de violation de ces conditions ou d'activité suspecte."
          },
          section6: {
            title: "6. Limitation de responsabilité",
            content: "CMTRIP n'est pas responsable des retards, annulations ou perturbations causés par des prestataires de services tiers."
          }
        },
        es: {
          backToHome: "Volver al inicio",
          title: "Términos de servicio de CMTRIP",
          section1: {
            title: "1. Obligaciones del usuario",
            content: "Al usar CMTRIP, acepta proporcionar información de registro precisa y cumplir con todas las reglas de la plataforma. El acceso no autorizado, el scraping de datos o las reservas fraudulentas están estrictamente prohibidos y pueden resultar en la suspensión de la cuenta."
          },
          section2: {
            title: "2. Términos de reserva",
            content: "Todas las reservas están sujetas a los términos del proveedor de servicios (por ejemplo, tarifas de cancelación, restricciones de edad). CMTRIP actúa como intermediario y no es responsable de disputas directas entre usuarios y proveedores."
          },
          section3: {
            title: "3. Propiedad intelectual",
            content: "El logotipo de CMTRIP, el diseño de la plataforma y el contenido (por ejemplo, guías de destinos) están protegidos por derechos de autor. No puede reproducir o distribuir ninguna parte de la plataforma sin consentimiento previo por escrito."
          },
          section4: {
            title: "4. Ley aplicable",
            content: "Estos términos se rigen por las leyes de [su jurisdicción]. Las disputas se resolverán mediante arbitraje vinculante en [ciudad]."
          },
          section5: {
            title: "5. Terminación",
            content: "CMTRIP se reserva el derecho de terminar su cuenta en cualquier momento por violaciones de estos términos o actividad sospechosa."
          },
          section6: {
            title: "6. Limitación de responsabilidad",
            content: "CMTRIP no es responsable por retrasos, cancelaciones o interrupciones causadas por proveedores de servicios de terceros."
          }
        },
        ru: {
          backToHome: "Вернуться на главную",
          title: "Условия обслуживания CMTRIP",
          section1: {
            title: "1. Обязанности пользователя",
            content: "Используя CMTRIP, вы соглашаетесь предоставлять точную регистрационную информацию и соблюдать все правила платформы. Несанкционированный доступ, сбор данных или мошеннические бронирования строго запрещены и могут привести к блокировке аккаунта."
          },
          section2: {
            title: "2. Условия бронирования",
            content: "Все бронирования подчиняются условиям поставщика услуг (например, штрафы за отмену, возрастные ограничения). CMTRIP выступает посредником и не несет ответственности за прямые споры между пользователями и поставщиками."
          },
          section3: {
            title: "3. Интеллектуальная собственность",
            content: "Логотип CMTRIP, дизайн платформы и контент (например, путеводители) защищены авторским правом. Вы не можете воспроизводить или распространять любую часть платформы без предварительного письменного согласия."
          },
          section4: {
            title: "4. Применимое право",
            content: "Настоящие условия регулируются законами [вашей юрисдикции]. Споры будут разрешаться путем обязательного арбитража в [городе]."
          },
          section5: {
            title: "5. Прекращение",
            content: "CMTRIP оставляет за собой право прекратить действие вашей учетной записи в любое время за нарушение этих условий или подозрительную активность."
          },
          section6: {
            title: "6. Ограничение ответственности",
            content: "CMTRIP не несет ответственности за задержки, отмены или перебои, вызванные сторонними поставщиками услуг."
          }
        },
        ar: {
          backToHome: "العودة إلى الصفحة الرئيسية",
          title: "شروط خدمة CMTRIP",
          section1: {
            title: "1. التزامات المستخدم",
            content: "باستخدامك لـ CMTRIP، فإنك توافق على تقديم معلومات تسجيل دقيقة والامتثال لجميع قواعد المنصة. يُحظر تمامًا الوصول غير المصرح به، أو جمع البيانات، أو الحجوزات الاحتيالية، وقد يؤدي ذلك إلى تعليق الحساب."
          },
          section2: {
            title: "2. شروط الحجز",
            content: "تخضع جميع الحجوزات لشروط مزود الخدمة (مثل رسوم الإلغاء، أو القيود العمرية). يعمل CMTRIP كوسيط ولا يتحمل مسؤولية النزاعات المباشرة بين المستخدمين والمزودين."
          },
          section3: {
            title: "3. الملكية الفكرية",
            content: "شعار CMTRIP وتصميم المنصة والمحتوى (مثل أدلة الوجهات) محمي بحقوق الطبع والنشر. لا يجوز لك نسخ أو توزيع أي جزء من المنصة دون موافقة خطية مسبقة."
          },
          section4: {
            title: "4. القانون الحاكم",
            content: "تخضع هذه الشروط لقوانين [ولايتك القضائية]. سيتم حل النزاعات من خلال التحكيم الملزم في [المدينة]."
          },
          section5: {
            title: "5. إنهاء الحساب",
            content: "تحتفظ CMTRIP بالحق في إنهاء حسابك في أي وقت إذا انتهكت هذه الشروط أو تم الاشتباه في نشاط غير قانوني."
          },
          section6: {
            title: "6. تحديد المسؤولية",
            content: "لا تتحمل CMTRIP أي مسؤولية عن التأخيرات أو الإلغاءات أو الانقطاعات الناتجة عن مزودي الخدمة من الأطراف الثالثة."
          }
        }
      }
    }
  },
  computed: {
    currentTranslations() {
      return this.translations[this.currentLanguage] || this.translations.en;
    }
  },
  // created() {
  //   const savedLang = localStorage.getItem('appLanguage');
  //   if (savedLang && this.supportedLanguages.some(l => l.code === savedLang)) {
  //     this.currentLanguage = savedLang;
  //   }
  // },
  methods: {
    changeLanguage(lang) {
      this.currentLanguage = lang;
      // localStorage.setItem('appLanguage', lang);
    }
  }
}
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