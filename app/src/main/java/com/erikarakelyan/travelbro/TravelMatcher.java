package com.erikarakelyan.travelbro;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TravelMatcher {

    private static final List<DestinationRule> destinations = new ArrayList<>();

    static {
        // ===== BEACH + HOT =====
        destinations.add(new DestinationRule(
            new TravelDestination("Таиланд", "Краби", "🇹🇭",
                "Изумрудные воды, известняковые скалы и идеальный пляжный отдых под пальмами. Краби — жемчужина Таиланда, сочетающая дикую природу и комфорт.",
                "🏖️ Пляж Рейли · 🧗 Скалолазание · 🤿 Снорклинг · 🌅 Закаты · 🐘 Слоновий заповедник",
                "Ноябрь – Апрель",
                95),
            new String[]{"hot", "beach", null, null, null, "asian", null}
        ));

        destinations.add(new DestinationRule(
            new TravelDestination("Мальдивы", "Мале / Атоллы", "🇲🇻",
                "Кристально чистая вода, белоснежные пляжи и бунгало прямо над океаном. Рай на земле для тех, кто ищет уединение и роскошь.",
                "🏊 Снорклинг с манта-скатами · 🐠 Дайвинг · 🌺 СПА · 🌊 Водные виллы · 🐢 Черепахи",
                "Ноябрь – Апрель",
                98),
            new String[]{"hot", "beach", "luxury", null, "relax", null, null}
        ));

        destinations.add(new DestinationRule(
            new TravelDestination("Греция", "Санторини", "🇬🇷",
                "Белоснежные домики на скалах, синие купола церквей и лучшие закаты в мире. Романтика и история в каждом уголке.",
                "🌅 Закат в Ия · 🍷 Местное вино · 🏛️ Акротири · ⛵ Морские прогулки · 🥗 Греческая кухня",
                "Май – Октябрь",
                92),
            new String[]{"mild", "beach", null, "couple", null, "european", null}
        ));

        destinations.add(new DestinationRule(
            new TravelDestination("Испания", "Барселона", "🇪🇸",
                "Архитектура Гауди, пляжи Барселонеты, тапас-бары и бурная ночная жизнь. Город, который никогда не спит.",
                "🏛️ Саграда Фамилия · 🎨 Парк Гуэль · 🍹 Пляжные бары · 💃 Фламенко · 🛍️ Шопинг",
                "Май – Октябрь",
                90),
            new String[]{"mild", null, "mid", null, null, "european", "week"}
        ));

        // ===== MOUNTAINS + ADVENTURE =====
        destinations.add(new DestinationRule(
            new TravelDestination("Непал", "Катманду / Покхара", "🇳🇵",
                "Вершины мира, древние храмы и душа Гималаев. Треккинг к базовому лагерю Эвереста — мечта каждого искателя приключений.",
                "🏔️ Треккинг Эверест BC · 🛕 Ступа Боднатх · 🪂 Параглайдинг · 🦅 Рассвет на Пун-Хилл · 🧘 Медитация",
                "Март – Май, Сентябрь – Ноябрь",
                96),
            new String[]{"cold", "mountains", null, null, "adventure", null, "twoweeks"}
        ));

        destinations.add(new DestinationRule(
            new TravelDestination("Швейцария", "Интерлакен", "🇨🇭",
                "Альпийские луга, кристальные озёра и адреналин прыжков с парашютом над горами. Сказочная природа круглый год.",
                "⛷️ Горные лыжи · 🪂 Прыжки с парашютом · 🚠 Канатные дороги · 🏔️ Юнгфрау · 🛶 Каяк",
                "Декабрь – Март (лыжи), Июнь – Август (треккинг)",
                88),
            new String[]{"cold", "mountains", "premium", null, "adventure", "european", null}
        ));

        destinations.add(new DestinationRule(
            new TravelDestination("Новая Зеландия", "Квинстаун", "🇳🇿",
                "Столица экстрима планеты: банджи-джампинг, рафтинг, горные лыжи и фьорды. Природа, которая снимает дух.",
                "🏄 Рафтинг · 🪂 Банджи-джамп · 🛥️ Фьорд Милфорд · 🦅 Скайдайвинг · 🎿 Горнолыжный курорт",
                "Декабрь – Февраль",
                94),
            new String[]{null, "mountains", "premium", null, "adventure", null, "twoweeks"}
        ));

        destinations.add(new DestinationRule(
            new TravelDestination("Грузия", "Тбилиси / Казбеги", "🇬🇪",
                "Древние башни на фоне Кавказских гор, душевное гостеприимство и лучшее вино в мире. Доступно и невероятно красиво.",
                "🏔️ Гора Казбек · ⛪ Монастырь Гергети · 🍷 Виноделие · 🛁 Серные бани · 🎭 Старый Тбилиси",
                "Май – Октябрь",
                91),
            new String[]{"mild", "mountains", "budget", null, null, null, "week"}
        ));

        // ===== CULTURE + HISTORY =====
        destinations.add(new DestinationRule(
            new TravelDestination("Япония", "Киото", "🇯🇵",
                "Тысяча храмов, чайные церемонии, цветущая сакура и традиции самураев. Прикосновение к живой истории человечества.",
                "⛩️ Храм Фусими Инари · 🌸 Арасияма · 🍵 Чайная церемония · 🎎 Гион (гейши) · 🦌 Олени Нары",
                "Март – Май, Октябрь – Ноябрь",
                97),
            new String[]{"mild", "culture", null, null, "photos", "asian", null}
        ));

        destinations.add(new DestinationRule(
            new TravelDestination("Италия", "Рим", "🇮🇹",
                "Вечный город с двухтысячелетней историей: Колизей, Ватикан, фонтаны и нескончаемая вкуснейшая еда.",
                "🏛️ Колизей · ⛪ Ватикан · ⛲ Фонтан Треви · 🍝 Паста Карбонара · 🖼️ Галерея Боргезе",
                "Апрель – Июнь, Сентябрь – Октябрь",
                93),
            new String[]{"mild", "culture", null, null, null, "european", "week"}
        ));

        destinations.add(new DestinationRule(
            new TravelDestination("Марокко", "Марракеш", "🇲🇦",
                "Лабиринты медины, дворцы и специи. Марракеш — сказка «1001 ночи» в реальной жизни.",
                "🕌 Площадь Джемаа-эль-Фна · 🎨 Сады Мажорель · 🐫 Пустыня Сахара · 🛍️ Базары · 🧖 Хаммам",
                "Март – Май, Сентябрь – Ноябрь",
                89),
            new String[]{"hot", "culture", "mid", null, "photos", "middle", null}
        ));

        destinations.add(new DestinationRule(
            new TravelDestination("Перу", "Куско / Мачу-Пикчу", "🇵🇪",
                "Цитадель инков в облаках — одно из семи чудес света. История, мистика и невероятные горные пейзажи.",
                "🏯 Мачу-Пикчу · 🚂 Поезд Hiram Bingham · 🌽 Тропа инков · 🦙 Ламы · 🏙️ Куско",
                "Май – Октябрь",
                95),
            new String[]{"mild", "culture", null, null, "adventure", "latin", "week"}
        ));

        // ===== NIGHTLIFE + PARTY =====
        destinations.add(new DestinationRule(
            new TravelDestination("ОАЭ", "Дубай", "🇦🇪",
                "Самый высокий небоскрёб, золотые торговые центры и пустыня прямо за городом. Роскошь без ограничений.",
                "🗼 Бурдж-Халифа · 🏝️ Острова Пальма · 🛍️ Дубай Молл · 🏜️ Сафари в пустыне · 🎢 Парки аттракционов",
                "Ноябрь – Апрель",
                91),
            new String[]{"hot", "nightlife", "luxury", null, "shopping", "middle", null}
        ));

        destinations.add(new DestinationRule(
            new TravelDestination("Таиланд", "Бангкок", "🇹🇭",
                "Ультрасовременные клубы рядом с древними храмами. Уличная еда мирового уровня и безумная энергия 24/7.",
                "🌃 Ночная жизнь · 🛕 Ват Пхо · 🍜 Уличная еда · 🛍️ Рынок Чатучак · 🚤 Каналы",
                "Ноябрь – Март",
                88),
            new String[]{"hot", "nightlife", "budget", null, null, "asian", null}
        ));

        destinations.add(new DestinationRule(
            new TravelDestination("Португалия", "Лиссабон", "🇵🇹",
                "Самая гостеприимная столица Европы: фаду, паштел-де-ната, трамваи и одни из лучших закатов Атлантики.",
                "🚋 Трамвай 28 · 🎵 Фаду · 🏰 Замок Сан-Жоржи · 🌊 Синтра · 🍷 Портвейн",
                "Март – Октябрь",
                87),
            new String[]{"mild", "nightlife", "mid", null, null, "european", "week"}
        ));

        // ===== FAMILY =====
        destinations.add(new DestinationRule(
            new TravelDestination("Сингапур", "Сингапур", "🇸🇬",
                "Безопасный, чистый и захватывающий город-государство. Фантастические парки, Марина Бэй и кухня всех народов мира.",
                "🌳 Сады у залива · 🦁 Зоопарк · 🎡 Остров Сентоза · 🌉 Марина Бэй Сэндз · 🎢 Парк развлечений",
                "Круглый год",
                92),
            new String[]{"hot", null, null, "family", null, "asian", null}
        ));

        destinations.add(new DestinationRule(
            new TravelDestination("Хорватия", "Дубровник / Сплит", "🇭🇷",
                "Адриатическое побережье с кристальной водой, средневековыми крепостями и игра престолов живьём.",
                "🏰 Стены Дубровника · ⛵ Острова Корчула и Хвар · 🌊 Плитвицкие озёра · 🍷 Далматинское вино · 🐟 Морепродукты",
                "Июнь – Сентябрь",
                90),
            new String[]{"mild", "beach", "mid", "family", null, "european", "week"}
        ));

        // ===== BUDGET =====
        destinations.add(new DestinationRule(
            new TravelDestination("Вьетнам", "Хошимин / Ханой", "🇻🇳",
                "Франко-азиатская смесь культур, вкуснейшие фо-бо за копейки и потрясающие пейзажи бухты Халонг.",
                "🌊 Бухта Халонг · 🏙️ Хошимин · 🍜 Фо-бо · 🛵 Мотоциклы · 🏯 Старый Хой-Ан",
                "Ноябрь – Апрель (юг), Март – Сентябрь (север)",
                90),
            new String[]{"hot", null, "budget", null, null, "asian", null}
        ));

        destinations.add(new DestinationRule(
            new TravelDestination("Армения", "Ереван / Гарни", "🇦🇲",
                "Один из древнейших народов мира, монастыри в горах, гранатовый коньяк и невероятное гостеприимство.",
                "🏛️ Храм Гарни · ⛪ Монастырь Гегард · 🍷 Армянский коньяк · 🏔️ Гора Арарат · 🍽️ Долма",
                "Апрель – Октябрь",
                88),
            new String[]{"mild", "culture", "budget", null, null, "middle", "week"}
        ));

        // ===== PHOTOS / INSTAGRAM =====
        destinations.add(new DestinationRule(
            new TravelDestination("Исландия", "Рейкьявик", "🇮🇸",
                "Северное сияние, гейзеры, водопады и вулканические пейзажи. Марсианская природа на краю Земли.",
                "🌌 Северное сияние · 🌊 Голубая лагуна · 💧 Водопад Сельяландсфосс · 🐋 Киты · 🌋 Вулкан Эйяфьядлайёкюдль",
                "Сентябрь – Март (сияние), Июнь – Август (24-часовой день)",
                96),
            new String[]{"cold", null, "premium", null, "photos", null, "week"}
        ));

        destinations.add(new DestinationRule(
            new TravelDestination("Бали", "Убуд / Семиньяк", "🇮🇩",
                "Остров богов: рисовые террасы, храмы на утёсах и закат у знаменитого храма Тананг Лот.",
                "🌾 Рисовые террасы Тегалаланг · 🌊 Храм Танах Лот · 🐒 Лес обезьян · 🧘 Йога-ретриты · 🌅 Серфинг",
                "Апрель – Октябрь",
                93),
            new String[]{"hot", null, "mid", null, "photos", "asian", null}
        ));
    }

    public static TravelDestination findBestMatch(Map<String, String> answers) {
        TravelDestination best = null;
        int bestScore = -1;

        String[] keys = {"climate", "attraction", "budget", "travel", "priority", "cuisine", "duration"};

        for (DestinationRule rule : destinations) {
            int score = rule.destination.matchScore;

            for (int i = 0; i < keys.length; i++) {
                String userAnswer = answers.get(keys[i]);
                String ruleValue = rule.filters[i];

                if (ruleValue != null && userAnswer != null) {
                    if (userAnswer.equals(ruleValue)) {
                        score += 15;
                    } else {
                        score -= 8;
                    }
                }
            }

            if (score > bestScore) {
                bestScore = score;
                best = rule.destination;
            }
        }

        if (best == null) {
            // Default fallback
            best = new TravelDestination("Япония", "Токио", "🇯🇵",
                "Страна контрастов: высокие технологии и древние традиции в одном месте.",
                "⛩️ Храмы · 🍣 Суши · 🌸 Сакура · 🗼 Токийская башня · 🎌 Традиции",
                "Март – Май",
                85);
        }
        best.matchScore = Math.min(bestScore, 99);
        return best;
    }

    static class DestinationRule {
        TravelDestination destination;
        String[] filters; // [climate, attraction, budget, travel, priority, cuisine, duration]

        DestinationRule(TravelDestination destination, String[] filters) {
            this.destination = destination;
            this.filters = filters;
        }
    }
}
