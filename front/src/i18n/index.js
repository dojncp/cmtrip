// src/i18n/index.js

import { createI18n } from 'vue-i18n'

const messages = {
    en: {
        header: {
            currentUser: "Current User",
            logout: "Log Out",
            language: "Language"
        },
        menu: {
            home: "Home"
        },
        footer: {
            aboutUs: "About Us",
            github: "Github Store",
            email: "Email",
            fastLinks: "Fast Links",
            privacy: "Privacy Policies",
            terms: "Service Terms",
            help: "Help Center",
            rights: "All rights reserved.",
            slogan: "Support Your Trips",
        },
        home: {
            title: "Welcome to Travel Story Recording Platform",
            description: "This is a space created for travel enthusiasts: here, you can freely plan your upcoming trips or record every shining moment during your travels. Whether it's an exploration spanning several days or an afternoon encounter, it's all worth cherishing.",
            trip: {
                title: "Trip",
                description: "Record an overall trip arrangement, such as an unforgettable experience in Tokyo from January 1st to January 7th."
            },
            action: {
                title: "Action",
                description: "Detailed hourly or even second-by-second activity arrangements, such as visiting Asakusa from 12:00 to 13:00 on January 1st."
            },
            photos: "Upload travel photos and relive every step with yourself and friends."
        }
    },
    zh: {
        header: {
            currentUser: "当前用户",
            logout: "退出登录",
            language: "语言 [Language]"
        },
        menu: {
            home: "首页"
        },
        footer: {
            aboutUs: "关于我们",
            github: "Github仓库",
            email: "电子邮箱",
            fastLinks: "快速链接",
            privacy: "隐私政策",
            terms: "服务条款",
            help: "帮助中心",
            rights: "版权所有。",
            slogan: "为您的旅程提供支持"
        },
        home: {
            title: "欢迎来到旅行故事记录平台",
            description: "这是一个为热爱旅行的人们打造的空间：在这里，您可以自由规划即将开始的旅行日程，或者记录旅途中每一个闪光的瞬间。无论是一次横跨数天的探索，还是某个午后的偶遇，都值得被珍藏。",
            trip: {
                title: "旅行（Trip）",
                description: "记录一次旅行的整体安排，例如 1 月 1 日至 1 月 7 日在东京的难忘体验。"
            },
            action: {
                title: "行程（Action）",
                description: "精细到每小时甚至每秒的活动安排，如 1 月 1 日 12:00~13:00 游览浅草。"
            },
            photos: "上传旅行照片，与自己和朋友一起重温每一个脚步。"
        }
    },
    fr: {
        header: {
            currentUser: "Utilisateur actuel",
            logout: "Se déconnecter",
            language: "Langue [Language]"
        },
        menu: {
            home: "Accueil"
        },
        footer: {
            aboutUs: "À propos",
            github: "Dépôt GitHub",
            email: "E-mail",
            fastLinks: "Liens rapides",
            privacy: "Politique de confidentialité",
            terms: "Conditions d'utilisation",
            help: "Centre d'aide",
            rights: "Tous droits réservés",
            slogan: "Soutien à vos voyages"
        },
        home: {
            title: "Bienvenue sur la plateforme d'enregistrement des histoires de voyage",
            description: "C'est un espace créé pour les amateurs de voyage : ici, vous pouvez librement planifier vos prochains voyages ou enregistrer chaque instant éclatant de vos voyages. Que ce soit une exploration de plusieurs jours ou une rencontre fortuite de l'après-midi, tout vaut la peine d'être chéri.",
            trip: {
                title: "Voyage",
                description: "Enregistrez un arrangement de voyage global, comme une expérience inoubliable à Tokyo du 1er janvier au 7 janvier."
            },
            action: {
                title: "Action",
                description: "Des arrangements d'activités détaillés à l'heure ou même à la seconde, comme visiter Asakusa de 12h00 à 13h00 le 1er janvier."
            },
            photos: "Téléchargez des photos de voyage et revivez chaque étape avec vous-même et vos amis."
        },
    },
    es: {
        header: {
            currentUser: "Usuario actual",
            logout: "Cerrar sesión",
            language: "Idioma [Language]"
        },
        menu: {
            home: "Inicio"
        },
        footer: {
            aboutUs: "Sobre nosotros",
            github: "Repositorio GitHub",
            email: "Correo electrónico",
            fastLinks: "Enlaces rápidos",
            privacy: "Política de privacidad",
            terms: "Términos de servicio",
            help: "Centro de ayuda",
            rights: "Todos los derechos reservados",
            slogan: "Apoyo a sus viajes"
        },
        home: {
            title: "Bienvenido a la plataforma de grabación de historias de viaje",
            description: "Este es un espacio creado para los amantes de los viajes: aquí, puedes planificar libremente tus próximos viajes o registrar cada momento brillante durante tus viajes. Ya sea una exploración de varios días o un encuentro casual de la tarde, todo vale la pena ser atesorado.",
            trip: {
                title: "Viaje (Trip)",
                description: "Registra un arreglo general de viaje, como una experiencia inolvidable en Tokio del 1 de enero al 7 de enero."
            },
            action: {
                title: "Acción (Action)",
                description: "Arreglos detallados de actividades por hora o incluso por segundo, como visitar Asakusa de 12:00 a 13:00 el 1 de enero."
            },
            photos: "Sube fotos de viaje y revive cada paso contigo mismo y tus amigos."
        }
    },
    ru: {
        header: {
            currentUser: "Текущий пользователь",
            logout: "Выйти",
            language: "Язык [Language]"
        },
        menu: {
            home: "Главная"
        },
        footer: {
            aboutUs: "О нас",
            github: "Репозиторий GitHub",
            email: "Электронная почта",
            fastLinks: "Быстрые ссылки",
            privacy: "Политика конфиденциальности",
            terms: "Условия обслуживания",
            help: "Центр поддержки",
            rights: "Все права защищены",
            slogan: "Поддержка ваших поездок"
        },
        home: {
            title: "Добро пожаловать на платформу записи историй путешествий",
            description: "Это пространство, созданное для любителей путешествий: здесь вы можете свободно планировать предстоящие поездки или записывать каждый яркий момент во время путешествий. Будь то исследование на несколько дней или случайная встреча днем, все это заслуживает бережного хранения.",
            trip: {
                title: "Путешествие (Trip)",
                description: "Запишите общий план поездки, например, незабываемый опыт в Токио с 1 января по 7 января."
            },
            action: {
                title: "Действие (Action)",
                description: "Детализированные расписания активностей на каждый час или даже каждую секунду, например, посещение Асакусы с 12:00 до 13:00 1 января."
            },
            photos: "Загрузите фотографии путешествий и переживите каждый шаг вместе с собой и друзьями."
        }
    },
    ar: {
        header: {
            currentUser: "المستخدم الحالي",
            logout: "تسجيل الخروج",
            language: "[Language] اللغة"
        },
        menu: {
            home: "الصفحة الرئيسية"
        },
        footer: {
            aboutUs: "معلومات عنا",
            github: "مستودع جيت هاب",
            email: "البريد الإلكتروني",
            fastLinks: "روابط سريعة",
            privacy: "سياسة الخصوصية",
            terms: "شروط الخدمة",
            help: "مركز المساعدة",
            rights: "جميع الحقوق محفوظة",
            slogan: "دعم رحلاتك"
        },
        home: {
            title: "مرحبًا بكم في منصة تسجيل قصص السفر",
            description: "هذا مساحة أُنشئت لعشاق السفر: هنا، يمكنكم بحرية تخطيط رحلاتكم القادمة أو تسجيل كل لحظة مشرقة خلال رحلاتكم. سواء كانت استكشافًا يمتد لعدة أيام أو لقاءً عابرًا بعد الظهر، كل شيء يستحق الحفظ.",
            trip: {
                title: "رحلة (Trip)",
                description: "سجل ترتيبًا عامًا للرحلة، مثل تجربة لا تُنسى في طوكيو من الأول من يناير إلى السابع من يناير."
            },
            action: {
                title: "إجراء (Action)",
                description: "ترتيبات نشاطية مفصلة على مدار الساعة أو حتى بالثانية، مثل زيارة أساكوسا من الثانية عشرة إلى الواحدة ظهرًا في الأول من يناير."
            },
            photos: "قم بتحميل صور السفر واستعد كل خطوة مع نفسك وأصدقائك."
        }
    },
    jp: {
        header: {
            currentUser: "現在のユーザー",
            logout: "ログアウト",
            language: "言語 [Language]"
        },
        menu: {
            home: "ホーム"
        },
        footer: {
            aboutUs: "私たちについて",
            github: "GitHubリポジトリ",
            email: "メールアドレス",
            fastLinks: "クイックリンク",
            privacy: "プライバシーポリシー",
            terms: "利用規約",
            help: "ヘルプセンター",
            rights: "無断複写・転載を禁じます",
            slogan: "旅をサポート"
        },
        home: {
            title: "旅行ストーリー記録プラットフォームへようこそ",
            description: "旅行を愛する人々のために作られた空間です。ここでは、これから始まる旅行スケジュールを自由に計画したり、旅の中で輝く瞬間を記録したりすることができます。数日間にわたる探検であれ、午後の偶然の出会いであれ、すべてが大切にされるべきです。",
            trip: {
                title: "旅行（Trip）",
                description: "例えば、1月1日から1月7日までの東京での忘れられない体験のように、旅行全体のスケジュールを記録してください。"
            },
            action: {
                title: "行程（Action）",
                description: "1月1日の12:00から13:00までの浅草訪問のように、毎時間、あるいは毎秒まで細かく活動スケジュールを記録してください。"
            },
            photos: "旅行写真をアップロードし、自分と友達と一緒に一歩一歩を振り返ってください。"
        }
    }
}

const i18n = createI18n({
    legacy: false,
    locale: 'en',
    fallbackLocale: 'en',
    globalInjection: true,
    messages,
})

export default i18n

