# 🌍 CMTRIP Guide
© 2025 dojncp. All rights reserved.

[English](#english) | [中文](#中文) | [Français](#français) | [Español](#español) | [Русский](#русский) | [العربية](#العربية) | [日本語](#日本語)

---

## English

### Welcome
Welcome to **cm-trip**, a framework developed in 2025 for travel planning and trip logging.

### Overview
**(1) Platform & Tech Stack**  
Currently available only as a web app, built with Java + Spring Boot + MyBatis Plus on the backend and Vue3 + JavaScript on the frontend. The interface is clean and user-friendly, making it ideal for both direct use and secondary development.  
An iOS version is under development.

**(2) Authentication**  
Users must register and log in to use the system. Password recovery and modification features are coming soon.  
It uses **sa-token** (official docs: https://sa-token.cc/index.html) with a Role-User-Permission hierarchy to manage access. Developers can use annotations for flexible access control.

**(3) Core Features**  
Each **trip** can contain multiple **actions**. Users can upload one image per trip or action. Future updates may include budget estimation via travel pass suggestions.

### How to Use

**Backend**  
Requires Redis (port 6379), MySQL (port 3306), and JDK 17.  
The backend is in `/back` and runs on `localhost:8081`.

- **Database**: Execute the `.sql` file in your MySQL environment to create a `cmtrip` database.
- **Config**: Check `application.yml` under `/resources`.
- **Run**: Use an IDE to configure and run `CmtripApplication.java`.

**Frontend**  
Requires `Node.js` and `npm`.  
The frontend is in `/front` and runs on `localhost:5173`.

- **Install dependencies**: `npm install`
- **Run**: `npm run dev`

### Special Thanks
Thanks to [irasutoya](https://www.irasutoya.com/) for the icon artwork.

### Contact
For tech issues, email `dojncp@outlook.com` (English, Simplified/Traditional Chinese). Response not guaranteed.

### Notes
- Open-source under the MIT License. Not recommended for commercial use.
- This is the **only** official release. If not updated by `dojncp`, all copies elsewhere are subject to infringement claims.

---

## 中文

### 欢迎辞
欢迎使用 **cm-trip**，一款开发于2025年，适用于旅行和行程规划及记录的程序框架。

### 程序简介
**（1）适配平台及技术栈**  
目前仅提供Web版本，后端采用 Java + Spring Boot + MyBatis Plus，前端采用 Vue3 + JavaScript，界面简洁易用，也适合二次开发。  
iOS版本正在开发中，敬请期待。

**（2）用户登录及鉴权机制**  
程序需注册并登录后使用。后续将添加“找回密码”和“修改密码”等功能。  
采用 [sa-token](https://sa-token.cc/index.html) 框架，构建角色-用户-权限的三级结构，支持注解式鉴权，方便开发者使用。

**（3）原始业务结构**  
程序以“旅行（trip）”与“行程（action）”为两级结构，一次旅行可包含多个行程，每项都支持上传一张图片。未来版本可能支持旅行票据预算分析等功能。

### 使用指南

**后端**  
需安装 Redis（6379）、MySQL（3306）及 JDK17。  
后端位于 `/back`，运行于 `localhost:8081`。

- **数据库**：运行根目录下`.sql`文件以创建名为`cmtrip`的数据库。
- **配置**：编辑 `/resources/application.yml` 文件。
- **运行**：在IDE中配置并运行 `/CmtripApplication.java`。

**前端**  
需安装 `Node.js` 和 `npm`。  
前端位于 `/front`，运行于 `localhost:5173`。

- **安装依赖**：`npm install`
- **运行项目**：`npm run dev`

### 特别鸣谢
感谢 [irasutoya](https://www.irasutoya.com/) 提供图标支持。

### 联系方式
如遇技术问题，请用英文、简体中文或繁体中文发送邮件至 `dojncp@outlook.com`，不保证即时回复。

### 注意事项
- 本软件遵循 MIT 协议开源，因功能尚未完善，不推荐商用。
- 若非由 `dojncp` 更新此文件，其他平台所发布的类似版本视为侵权。

---

## Français

### Bienvenue
Bienvenue sur **cm-trip**, un cadre de développement créé en 2025 pour planifier et enregistrer vos voyages.

### Présentation
**(1) Plateformes et technologies**  
Web uniquement : Java + Spring Boot + MyBatis Plus pour le backend ; Vue3 + JS pour le frontend. Interface sobre et facile à utiliser. Version iOS en développement.

**(2) Authentification**  
Inscription et connexion obligatoires. Fonctions “réinitialisation” et “modification du mot de passe” à venir.  
Utilise [sa-token](https://sa-token.cc/index.html) pour gérer les rôles et permissions via annotations.

**(3) Fonctionnalités**  
Une excursion (trip) peut contenir plusieurs actions. Chaque élément peut comporter une image. Les prochaines versions pourront proposer des calculs de budget basés sur les pass touristiques.

### Utilisation

**Backend**  
Pré-requis : Redis (6379), MySQL (3306), JDK 17.  
Chemin : `/back`, port `8081`.

**Frontend**  
Pré-requis : Node.js, npm.  
Chemin : `/front`, port `5173`.

### Remerciements
Merci à [irasutoya](https://www.irasutoya.com/) pour les icônes.

### Contact
Email : `dojncp@outlook.com` (anglais, chinois simplifié/traditionnel).

---

## Español

### Bienvenida
Bienvenido a **cm-trip**, un marco creado en 2025 para la planificación y el registro de viajes.

### Descripción
**(1) Tecnologías**  
Web solamente: Java + Spring Boot + MyBatis Plus (backend), Vue3 + JS (frontend). Versión iOS en camino.

**(2) Autenticación**  
Requiere registro e inicio de sesión. Pronto incluirá recuperación de contraseña.  
Basado en [sa-token](https://sa-token.cc/index.html) para permisos por roles.

**(3) Funciones**  
Un viaje puede tener varias acciones. Cada entrada puede tener una imagen. Futuras versiones incluirán cálculo de presupuesto.

### Uso

**Backend**  
Redis, MySQL, JDK 17 necesarios. Ejecutar en `localhost:8081`.

**Frontend**  
Node.js, npm requeridos. Ejecutar en `localhost:5173`.

### Agradecimientos
Gracias a [irasutoya](https://www.irasutoya.com/) por los íconos.

### Contacto
`dojncp@outlook.com` (inglés o chino).

---

## Русский

### Добро пожаловать
Добро пожаловать в **cm-trip** — фреймворк 2025 года для планирования и ведения путешествий.

### Обзор
**(1) Платформа и стек технологий**  
Только веб-версия: Java + Spring Boot + MyBatis Plus (сервер), Vue3 + JS (клиент). Разработка для iOS в процессе.

**(2) Аутентификация**  
Регистрация и вход обязательны. Используется [sa-token](https://sa-token.cc/index.html).

**(3) Основной функционал**  
Поездка (trip) содержит действия (action). Возможность загрузки изображений. Будущий функционал — расчёт бюджета.

### Использование
Бэкэнд: Redis, MySQL, JDK17. Фронтэнд: Node.js, npm.

### Благодарности
Спасибо [irasutoya](https://www.irasutoya.com/) за иконки.

### Контакты
`dojncp@outlook.com` (английский или китайский).

---

## العربية

### مرحبًا
مرحبًا بك في **cm-trip**، إطار عمل تم تطويره في عام 2025 لتخطيط الرحلات وتسجيلها.

### نظرة عامة
**(1) التقنيات**  
نسخة الويب فقط. الخلفية بـ Java + Spring Boot + MyBatis Plus. الواجهة بـ Vue3 + JS.

**(2) تسجيل الدخول**  
يتطلب تسجيل الدخول. يستخدم [sa-token](https://sa-token.cc/index.html) للتحكم في الصلاحيات.

**(3) الميزات**  
رحلة = trip، وكل رحلة تحتوي على عدة أنشطة = actions. يمكن رفع صورة لكل عنصر.

### الاستخدام
الخلفية: Redis، MySQL، JDK 17  
الواجهة: Node.js، npm

### شكر خاص
شكرًا لـ [irasutoya](https://www.irasutoya.com/) على الرموز.

### التواصل
`dojncp@outlook.com` (الإنجليزية أو الصينية)

---

## 日本語

### ようこそ
**cm-trip** へようこそ。2025年に開発された、旅行計画および記録用のフレームワークです。

### 概要
**(1) 対応プラットフォームと技術**  
現在はWeb版のみ。バックエンド：Java + Spring Boot + MyBatis Plus、フロントエンド：Vue3 + JS。iOS版は開発中です。

**(2) 認証機構**  
ログインが必要です。[sa-token](https://sa-token.cc/index.html) による権限管理。

**(3) 機能**  
旅行（trip）には複数の行動（action）を含めることができ、各項目に画像を添付可能。将来的には予算管理機能も追加予定。

### 使い方
バックエンド：Redis、MySQL、JDK17  
フロントエンド：Node.js、npm

### 感謝
[irasutoya](https://www.irasutoya.com/) に感謝します。

### 連絡先
技術的な質問は `dojncp@outlook.com` へ（英語または中国語対応）

---
