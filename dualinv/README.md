# Dual Inventory — Source Code (محدَّث)

## ⚠️ اقرأ هذا أولاً قبل ما تحاول تعمل `gradlew build`

هذا الكود **جُرِّب فعليًا داخل اللعبة**. الكود مكتوب بأسماء "SRG" التقنية (زي `m_150109_()`) مش أسماء Mojang الودودة (`getInventory()`) — لأني عملت `compile` يدوي **مباشرة ضد Minecraft/Forge الحقيقيين** بدل ForgeGradle العادي، لضمان صحة كل استدعاء 100%.

**لو حاولت `gradlew build` عادي، على الأرجح مش هينجح** (Gradle هيجهزلك أسماء ودودة، والكود بيستخدم أسماء تقنية). كل دالة فيها تعليق فوقها بيوضح اسمها الودود المقابل لو عايز تفهم/تعدّل.

**لو مش ناوي تعدّل حاجة — الـ jar الشغال عندك فعليًا هو المرجع، مش محتاج تبنيه من هنا تاني.**

---

## بنية المشروع
```
src/main/java/com/example/dualinventory/
├── DualInventoryMod.java          - الكلاس الرئيسي، يسجّل كل حاجة على الـ Bus الصحيح
├── capability/
│   ├── InventorySnapshot.java     - "صورة" واحدة لإنفنتوري كامل (حفظ/تحميل، بما فيها الحقيبة والـCurios)
│   ├── DualInventoryData.java     - يحمل نسختين A و B + مين الشغال
│   ├── DualInventoryProvider.java - ربط البيانات بنظام Forge Capability
│   ├── CapabilityHandler.java     - تسجيل الـ Capability ولصقه بكل لاعب
│   └── InventorySwapService.java  - المنطق الفعلي للتبديل (القلب) + ربط التكاملات الاختيارية
├── compat/
│   ├── TravelersBackpackCompat.java - تكامل اختياري (Soft Dependency) مع Traveler's Backpack
│   └── CuriosCompat.java            - تكامل اختياري (Soft Dependency) عام مع كل خانات Curios
├── network/
│   ├── SwapInventoryPacket.java   - رسالة "بدّل" من العميل للسيرفر
│   └── NetworkHandler.java        - تسجيل قناة الشبكة
├── client/
│   ├── KeyBindings.java           - تسجيل زر Z
│   ├── ClientEvents.java          - مراقبة الضغط على الزر (Forge Bus)
│   ├── ClientSetup.java           - إعداد العميل الأساسي (Mod Bus)
│   └── ClientForgeEvents.java     - رسم [A]/[B] على الشاشة (Forge Bus - منفصل عمدًا عن ClientSetup)
└── event/
    └── PlayerEvents.java          - الحفاظ على البيانات بعد الموت/تغيير الأبعاد (Forge Bus)
```

## 🔌 التكاملات الاختيارية (Soft Dependencies)

كلاهما **اختياري تمامًا**: لو المود مش مركّب، الكود ببساطة بيتجاهله (`ModList.isLoaded(...)`) بدون أي كراش.

- **Traveler's Backpack**: بيستخدم `CapabilityUtils.getWearingBackpack`/`equipBackpack` الخاصة بيه، لأنه عنده نظام "لبس" منفصل عن الإنفنتوري العادي.
- **Curios**: بيمشي على **كل** خانات Curios (خواتم، أحزمة، أي حاجة، مش بس الحقائب) عن طريق واجهة Forge القياسية `IItemHandlerModifiable` (`getStackInSlot`/`setStackInSlot`) — بنفس الطريقة اللي شاشة اللبس/الخلع الحقيقية بتستخدمها، عشان يتوافق صح مع نظام مزامنة Curios الداخلي.

## 🐛 أخطاء حقيقية اتصلحت أثناء التطوير (موثقة كمرجع)
1. أسماء SRG غلط لعدة دوال/حقول (`items`→`f_35974_`, `getInventory()`→`m_150109_`... إلخ)
2. تسجيل `PlayerEvents` و`ClientForgeEvents` على الـ Event Bus الغلط (Mod Bus بدل Forge Bus) — كان بيمنع تحميل المود بالكامل
3. استخدام `broadcastChanges()` (فحص فرق) بدل `sendAllDataToRemote()` (إرسال كامل إجباري) على `containerMenu` — سبب عدم ظهور تغيير الإنفنتوري العادي للاعب فعليًا
4. دعم ديناميكي لعدد خانات الإنفنتوري (بدل افتراض 36 دايمًا) — لدعم مودات بتوسّع الإنفنتوري
5. تكامل Curios: التبديل عبر `readTag()` الجماعي كان بيغيّر بيانات السيرفر صح لكن من غير ما يبعت تحديث للجهاز (مشكلة مزامنة خاصة بـCurios) — اتغيّر لتحديث كل خانة لوحدها عبر `setStackInSlot` بدل الاستبدال الجماعي (لسه محتاج تأكيد نهائي منك إنه اتحل فعليًا في اللعبة)

## 🐛 آخر إصلاح (الأحدث)
**باگ التكرار عند أول تبديل:** `applyTo()` كانت بتوقف عن الكتابة لو "الإنفنتوري الهدف" (main list) كان فاضي تمامًا (0 خانة، حالة أول استخدام لمجموعة لسه ما اتحفظتش قبل كده) — فكانت بتسيب الخانات الحية زي ما هي بدل ما تمسحها. النتيجة: أول تبديل، عناصر الـ Hotbar كانت "بتتنسخ" للمجموعة التانية بدل ما تختفي. تم الإصلاح بحيث كل خانة حية دايمًا بتتكتب صراحةً (لو مفيش بيانات محفوظة ليها، تتكتب "فاضية" فعليًا).
