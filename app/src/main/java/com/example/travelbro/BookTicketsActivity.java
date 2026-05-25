package com.example.travelbro;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;
import org.json.*;
import java.io.IOException;
import java.util.*;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class BookTicketsActivity extends AppCompatActivity {

    private static final String DUFFEL_TOKEN = "Example";

    private static final Map<String, String> CITY_IATA = new LinkedHashMap<>();
    static {
        // ═══ РОССИЯ ═══
        CITY_IATA.put("Москва", "MOW");           CITY_IATA.put("Moscow", "MOW");
        CITY_IATA.put("Санкт-Петербург", "LED");  CITY_IATA.put("Saint Petersburg", "LED");
        CITY_IATA.put("Питер", "LED");
        CITY_IATA.put("Новосибирск", "OVB");      CITY_IATA.put("Novosibirsk", "OVB");
        CITY_IATA.put("Екатеринбург", "SVX");     CITY_IATA.put("Yekaterinburg", "SVX");
        CITY_IATA.put("Казань", "KZN");           CITY_IATA.put("Kazan", "KZN");
        CITY_IATA.put("Краснодар", "KRR");        CITY_IATA.put("Krasnodar", "KRR");
        CITY_IATA.put("Сочи", "AER");             CITY_IATA.put("Sochi", "AER");
        CITY_IATA.put("Ростов-на-Дону", "ROV");   CITY_IATA.put("Rostov", "ROV");
        CITY_IATA.put("Уфа", "UFA");              CITY_IATA.put("Ufa", "UFA");
        CITY_IATA.put("Самара", "KUF");           CITY_IATA.put("Samara", "KUF");
        CITY_IATA.put("Пермь", "PEE");            CITY_IATA.put("Perm", "PEE");
        CITY_IATA.put("Воронеж", "VOZ");          CITY_IATA.put("Voronezh", "VOZ");
        CITY_IATA.put("Волгоград", "VOG");        CITY_IATA.put("Volgograd", "VOG");
        CITY_IATA.put("Красноярск", "KJA");       CITY_IATA.put("Krasnoyarsk", "KJA");
        CITY_IATA.put("Иркутск", "IKT");          CITY_IATA.put("Irkutsk", "IKT");
        CITY_IATA.put("Владивосток", "VVO");      CITY_IATA.put("Vladivostok", "VVO");
        CITY_IATA.put("Хабаровск", "KHV");        CITY_IATA.put("Khabarovsk", "KHV");
        CITY_IATA.put("Омск", "OMS");             CITY_IATA.put("Omsk", "OMS");
        CITY_IATA.put("Тюмень", "TJM");           CITY_IATA.put("Tyumen", "TJM");
        CITY_IATA.put("Нижний Новгород", "GOJ");  CITY_IATA.put("Nizhny Novgorod", "GOJ");
        CITY_IATA.put("Калининград", "KGD");      CITY_IATA.put("Kaliningrad", "KGD");
        CITY_IATA.put("Мурманск", "MMK");         CITY_IATA.put("Murmansk", "MMK");
        CITY_IATA.put("Архангельск", "ARH");      CITY_IATA.put("Arkhangelsk", "ARH");
        CITY_IATA.put("Якутск", "YKS");           CITY_IATA.put("Yakutsk", "YKS");
        CITY_IATA.put("Челябинск", "CEK");        CITY_IATA.put("Chelyabinsk", "CEK");
        CITY_IATA.put("Барнаул", "BAX");
        CITY_IATA.put("Кемерово", "KEJ");
        CITY_IATA.put("Томск", "TOF");
        CITY_IATA.put("Саратов", "RTW");
        CITY_IATA.put("Махачкала", "MCX");
        CITY_IATA.put("Минеральные Воды", "MRV");
        CITY_IATA.put("Симферополь", "SIP");

        // ═══ СНГ ═══
        CITY_IATA.put("Ереван", "EVN");           CITY_IATA.put("Yerevan", "EVN");
        CITY_IATA.put("Тбилиси", "TBS");          CITY_IATA.put("Tbilisi", "TBS");
        CITY_IATA.put("Батуми", "BUS");           CITY_IATA.put("Batumi", "BUS");
        CITY_IATA.put("Кутаиси", "KUT");          CITY_IATA.put("Kutaisi", "KUT");
        CITY_IATA.put("Баку", "GYD");             CITY_IATA.put("Baku", "GYD");
        CITY_IATA.put("Алматы", "ALA");           CITY_IATA.put("Almaty", "ALA");
        CITY_IATA.put("Астана", "NQZ");           CITY_IATA.put("Astana", "NQZ");
        CITY_IATA.put("Ташкент", "TAS");          CITY_IATA.put("Tashkent", "TAS");
        CITY_IATA.put("Самарканд", "SKD");        CITY_IATA.put("Samarkand", "SKD");
        CITY_IATA.put("Бишкек", "FRU");           CITY_IATA.put("Bishkek", "FRU");
        CITY_IATA.put("Душанбе", "DYU");          CITY_IATA.put("Dushanbe", "DYU");
        CITY_IATA.put("Минск", "MSQ");            CITY_IATA.put("Minsk", "MSQ");
        CITY_IATA.put("Киев", "KBP");             CITY_IATA.put("Kyiv", "KBP");
        CITY_IATA.put("Одесса", "ODS");           CITY_IATA.put("Odessa", "ODS");
        CITY_IATA.put("Кишинёв", "KIV");          CITY_IATA.put("Chisinau", "KIV");
        CITY_IATA.put("Ашхабад", "ASB");          CITY_IATA.put("Ashgabat", "ASB");

        // ═══ ТУРЦИЯ ═══
        CITY_IATA.put("Стамбул", "IST");          CITY_IATA.put("Istanbul", "IST");
        CITY_IATA.put("Анталья", "AYT");          CITY_IATA.put("Antalya", "AYT");
        CITY_IATA.put("Анкара", "ESB");           CITY_IATA.put("Ankara", "ESB");
        CITY_IATA.put("Измир", "ADB");            CITY_IATA.put("Izmir", "ADB");
        CITY_IATA.put("Бодрум", "BJV");           CITY_IATA.put("Bodrum", "BJV");
        CITY_IATA.put("Даламан", "DLM");          CITY_IATA.put("Dalaman", "DLM");
        CITY_IATA.put("Газипаша", "GZP");
        CITY_IATA.put("Трабзон", "TZX");          CITY_IATA.put("Trabzon", "TZX");
        CITY_IATA.put("Каппадокия", "NAV");

        // ═══ БЛИЖНИЙ ВОСТОК ═══
        CITY_IATA.put("Дубай", "DXB");            CITY_IATA.put("Dubai", "DXB");
        CITY_IATA.put("Абу-Даби", "AUH");         CITY_IATA.put("Abu Dhabi", "AUH");
        CITY_IATA.put("Шарджа", "SHJ");           CITY_IATA.put("Sharjah", "SHJ");
        CITY_IATA.put("Доха", "DOH");             CITY_IATA.put("Doha", "DOH");
        CITY_IATA.put("Эр-Рияд", "RUH");         CITY_IATA.put("Riyadh", "RUH");
        CITY_IATA.put("Джидда", "JED");           CITY_IATA.put("Jeddah", "JED");
        CITY_IATA.put("Бейрут", "BEY");           CITY_IATA.put("Beirut", "BEY");
        CITY_IATA.put("Амман", "AMM");            CITY_IATA.put("Amman", "AMM");
        CITY_IATA.put("Тель-Авив", "TLV");        CITY_IATA.put("Tel Aviv", "TLV");
        CITY_IATA.put("Маскат", "MCT");           CITY_IATA.put("Muscat", "MCT");
        CITY_IATA.put("Кувейт", "KWI");           CITY_IATA.put("Kuwait", "KWI");
        CITY_IATA.put("Тегеран", "IKA");          CITY_IATA.put("Tehran", "IKA");

        // ═══ АФРИКА ═══
        CITY_IATA.put("Каир", "CAI");             CITY_IATA.put("Cairo", "CAI");
        CITY_IATA.put("Хургада", "HRG");          CITY_IATA.put("Hurghada", "HRG");
        CITY_IATA.put("Шарм-эль-Шейх", "SSH");   CITY_IATA.put("Sharm el-Sheikh", "SSH");
        CITY_IATA.put("Луксор", "LXR");           CITY_IATA.put("Luxor", "LXR");
        CITY_IATA.put("Марракеш", "RAK");         CITY_IATA.put("Marrakech", "RAK");
        CITY_IATA.put("Касабланка", "CMN");       CITY_IATA.put("Casablanca", "CMN");
        CITY_IATA.put("Тунис", "TUN");            CITY_IATA.put("Tunis", "TUN");
        CITY_IATA.put("Найроби", "NBO");          CITY_IATA.put("Nairobi", "NBO");
        CITY_IATA.put("Занзибар", "ZNZ");         CITY_IATA.put("Zanzibar", "ZNZ");
        CITY_IATA.put("Йоханнесбург", "JNB");     CITY_IATA.put("Johannesburg", "JNB");
        CITY_IATA.put("Кейптаун", "CPT");         CITY_IATA.put("Cape Town", "CPT");
        CITY_IATA.put("Аккра", "ACC");            CITY_IATA.put("Accra", "ACC");
        CITY_IATA.put("Лагос", "LOS");            CITY_IATA.put("Lagos", "LOS");
        CITY_IATA.put("Аддис-Абеба", "ADD");      CITY_IATA.put("Addis Ababa", "ADD");
        CITY_IATA.put("Дар-эс-Салам", "DAR");     CITY_IATA.put("Dar es Salaam", "DAR");

        // ═══ ЮЖНАЯ И ЮГО-ВОСТОЧНАЯ АЗИЯ ═══
        CITY_IATA.put("Бангкок", "BKK");          CITY_IATA.put("Bangkok", "BKK");
        CITY_IATA.put("Пхукет", "HKT");           CITY_IATA.put("Phuket", "HKT");
        CITY_IATA.put("Чиангмай", "CNX");         CITY_IATA.put("Chiang Mai", "CNX");
        CITY_IATA.put("Сингапур", "SIN");         CITY_IATA.put("Singapore", "SIN");
        CITY_IATA.put("Куала-Лумпур", "KUL");     CITY_IATA.put("Kuala Lumpur", "KUL");
        CITY_IATA.put("Бали", "DPS");             CITY_IATA.put("Bali", "DPS");
        CITY_IATA.put("Джакарта", "CGK");         CITY_IATA.put("Jakarta", "CGK");
        CITY_IATA.put("Манила", "MNL");           CITY_IATA.put("Manila", "MNL");
        CITY_IATA.put("Себу", "CEB");             CITY_IATA.put("Cebu", "CEB");
        CITY_IATA.put("Ханой", "HAN");            CITY_IATA.put("Hanoi", "HAN");
        CITY_IATA.put("Хошимин", "SGN");          CITY_IATA.put("Ho Chi Minh", "SGN");
        CITY_IATA.put("Дананг", "DAD");           CITY_IATA.put("Da Nang", "DAD");
        CITY_IATA.put("Пномпень", "PNH");         CITY_IATA.put("Phnom Penh", "PNH");
        CITY_IATA.put("Сиемреап", "REP");         CITY_IATA.put("Siem Reap", "REP");
        CITY_IATA.put("Янгон", "RGN");            CITY_IATA.put("Yangon", "RGN");
        CITY_IATA.put("Дели", "DEL");             CITY_IATA.put("Delhi", "DEL");
        CITY_IATA.put("Мумбаи", "BOM");           CITY_IATA.put("Mumbai", "BOM");
        CITY_IATA.put("Гоа", "GOI");              CITY_IATA.put("Goa", "GOI");
        CITY_IATA.put("Бангалор", "BLR");         CITY_IATA.put("Bangalore", "BLR");
        CITY_IATA.put("Ченнаи", "MAA");          CITY_IATA.put("Chennai", "MAA");
        CITY_IATA.put("Коломбо", "CMB");          CITY_IATA.put("Colombo", "CMB");
        CITY_IATA.put("Мале", "MLE");             CITY_IATA.put("Male", "MLE");
        CITY_IATA.put("Катманду", "KTM");         CITY_IATA.put("Kathmandu", "KTM");
        CITY_IATA.put("Карачи", "KHI");           CITY_IATA.put("Karachi", "KHI");
        CITY_IATA.put("Лахор", "LHE");            CITY_IATA.put("Lahore", "LHE");
        CITY_IATA.put("Дакка", "DAC");            CITY_IATA.put("Dhaka", "DAC");

        // ═══ ВОСТОЧНАЯ АЗИЯ ═══
        CITY_IATA.put("Токио", "NRT");            CITY_IATA.put("Tokyo", "NRT");
        CITY_IATA.put("Осака", "KIX");            CITY_IATA.put("Osaka", "KIX");
        CITY_IATA.put("Киото", "KIX");            CITY_IATA.put("Kyoto", "KIX");
        CITY_IATA.put("Саппоро", "CTS");          CITY_IATA.put("Sapporo", "CTS");
        CITY_IATA.put("Сеул", "ICN");             CITY_IATA.put("Seoul", "ICN");
        CITY_IATA.put("Пусан", "PUS");            CITY_IATA.put("Busan", "PUS");
        CITY_IATA.put("Пекин", "PEK");            CITY_IATA.put("Beijing", "PEK");
        CITY_IATA.put("Шанхай", "PVG");           CITY_IATA.put("Shanghai", "PVG");
        CITY_IATA.put("Гуанчжоу", "CAN");         CITY_IATA.put("Guangzhou", "CAN");
        CITY_IATA.put("Шэньчжэнь", "SZX");        CITY_IATA.put("Shenzhen", "SZX");
        CITY_IATA.put("Чэнду", "CTU");            CITY_IATA.put("Chengdu", "CTU");
        CITY_IATA.put("Гонконг", "HKG");          CITY_IATA.put("Hong Kong", "HKG");
        CITY_IATA.put("Макао", "MFM");            CITY_IATA.put("Macau", "MFM");
        CITY_IATA.put("Тайбэй", "TPE");           CITY_IATA.put("Taipei", "TPE");
        CITY_IATA.put("Улан-Батор", "ULN");       CITY_IATA.put("Ulaanbaatar", "ULN");

        // ═══ ЗАПАДНАЯ ЕВРОПА ═══
        CITY_IATA.put("Лондон", "LHR");           CITY_IATA.put("London", "LHR");
        CITY_IATA.put("Лондон Гатвик", "LGW");    CITY_IATA.put("London Gatwick", "LGW");
        CITY_IATA.put("Манчестер", "MAN");         CITY_IATA.put("Manchester", "MAN");
        CITY_IATA.put("Эдинбург", "EDI");          CITY_IATA.put("Edinburgh", "EDI");
        CITY_IATA.put("Дублин", "DUB");            CITY_IATA.put("Dublin", "DUB");
        CITY_IATA.put("Париж", "CDG");             CITY_IATA.put("Paris", "CDG");
        CITY_IATA.put("Ницца", "NCE");             CITY_IATA.put("Nice", "NCE");
        CITY_IATA.put("Лион", "LYS");              CITY_IATA.put("Lyon", "LYS");
        CITY_IATA.put("Марсель", "MRS");           CITY_IATA.put("Marseille", "MRS");
        CITY_IATA.put("Берлин", "BER");            CITY_IATA.put("Berlin", "BER");
        CITY_IATA.put("Мюнхен", "MUC");            CITY_IATA.put("Munich", "MUC");
        CITY_IATA.put("Франкфурт", "FRA");         CITY_IATA.put("Frankfurt", "FRA");
        CITY_IATA.put("Гамбург", "HAM");           CITY_IATA.put("Hamburg", "HAM");
        CITY_IATA.put("Дюссельдорф", "DUS");       CITY_IATA.put("Dusseldorf", "DUS");
        CITY_IATA.put("Кёльн", "CGN");             CITY_IATA.put("Cologne", "CGN");
        CITY_IATA.put("Штутгарт", "STR");          CITY_IATA.put("Stuttgart", "STR");
        CITY_IATA.put("Мадрид", "MAD");            CITY_IATA.put("Madrid", "MAD");
        CITY_IATA.put("Барселона", "BCN");         CITY_IATA.put("Barcelona", "BCN");
        CITY_IATA.put("Валенсия", "VLC");          CITY_IATA.put("Valencia", "VLC");
        CITY_IATA.put("Севилья", "SVQ");           CITY_IATA.put("Seville", "SVQ");
        CITY_IATA.put("Малага", "AGP");            CITY_IATA.put("Malaga", "AGP");
        CITY_IATA.put("Бильбао", "BIO");           CITY_IATA.put("Bilbao", "BIO");
        CITY_IATA.put("Пальма", "PMI");            CITY_IATA.put("Palma", "PMI");
        CITY_IATA.put("Ибица", "IBZ");             CITY_IATA.put("Ibiza", "IBZ");
        CITY_IATA.put("Тенерифе", "TFS");          CITY_IATA.put("Tenerife", "TFS");
        CITY_IATA.put("Лас-Пальмас", "LPA");       CITY_IATA.put("Las Palmas", "LPA");
        CITY_IATA.put("Рим", "FCO");               CITY_IATA.put("Rome", "FCO");
        CITY_IATA.put("Милан", "MXP");             CITY_IATA.put("Milan", "MXP");
        CITY_IATA.put("Венеция", "VCE");           CITY_IATA.put("Venice", "VCE");
        CITY_IATA.put("Флоренция", "FLR");         CITY_IATA.put("Florence", "FLR");
        CITY_IATA.put("Неаполь", "NAP");           CITY_IATA.put("Naples", "NAP");
        CITY_IATA.put("Катания", "CTA");           CITY_IATA.put("Catania", "CTA");
        CITY_IATA.put("Палермо", "PMO");           CITY_IATA.put("Palermo", "PMO");
        CITY_IATA.put("Болонья", "BLQ");           CITY_IATA.put("Bologna", "BLQ");
        CITY_IATA.put("Амстердам", "AMS");         CITY_IATA.put("Amsterdam", "AMS");
        CITY_IATA.put("Брюссель", "BRU");          CITY_IATA.put("Brussels", "BRU");
        CITY_IATA.put("Цюрих", "ZRH");             CITY_IATA.put("Zurich", "ZRH");
        CITY_IATA.put("Женева", "GVA");            CITY_IATA.put("Geneva", "GVA");
        CITY_IATA.put("Базель", "BSL");            CITY_IATA.put("Basel", "BSL");
        CITY_IATA.put("Вена", "VIE");              CITY_IATA.put("Vienna", "VIE");
        CITY_IATA.put("Зальцбург", "SZG");         CITY_IATA.put("Salzburg", "SZG");
        CITY_IATA.put("Лиссабон", "LIS");          CITY_IATA.put("Lisbon", "LIS");
        CITY_IATA.put("Порту", "OPO");             CITY_IATA.put("Porto", "OPO");
        CITY_IATA.put("Фару", "FAO");              CITY_IATA.put("Faro", "FAO");
        CITY_IATA.put("Хельсинки", "HEL");         CITY_IATA.put("Helsinki", "HEL");
        CITY_IATA.put("Стокгольм", "ARN");         CITY_IATA.put("Stockholm", "ARN");
        CITY_IATA.put("Гётеборг", "GOT");          CITY_IATA.put("Gothenburg", "GOT");
        CITY_IATA.put("Осло", "OSL");              CITY_IATA.put("Oslo", "OSL");
        CITY_IATA.put("Берген", "BGO");            CITY_IATA.put("Bergen", "BGO");
        CITY_IATA.put("Копенгаген", "CPH");        CITY_IATA.put("Copenhagen", "CPH");
        CITY_IATA.put("Рейкьявик", "KEF");         CITY_IATA.put("Reykjavik", "KEF");

        // ═══ ЦЕНТРАЛЬНАЯ И ВОСТОЧНАЯ ЕВРОПА ═══
        CITY_IATA.put("Варшава", "WAW");           CITY_IATA.put("Warsaw", "WAW");
        CITY_IATA.put("Краков", "KRK");            CITY_IATA.put("Krakow", "KRK");
        CITY_IATA.put("Гданьск", "GDN");           CITY_IATA.put("Gdansk", "GDN");
        CITY_IATA.put("Прага", "PRG");             CITY_IATA.put("Prague", "PRG");
        CITY_IATA.put("Брно", "BRQ");              CITY_IATA.put("Brno", "BRQ");
        CITY_IATA.put("Братислава", "BTS");        CITY_IATA.put("Bratislava", "BTS");
        CITY_IATA.put("Будапешт", "BUD");          CITY_IATA.put("Budapest", "BUD");
        CITY_IATA.put("Бухарест", "OTP");          CITY_IATA.put("Bucharest", "OTP");
        CITY_IATA.put("Клуж", "CLJ");              CITY_IATA.put("Cluj", "CLJ");
        CITY_IATA.put("София", "SOF");             CITY_IATA.put("Sofia", "SOF");
        CITY_IATA.put("Варна", "VAR");             CITY_IATA.put("Varna", "VAR");
        CITY_IATA.put("Бургас", "BOJ");            CITY_IATA.put("Burgas", "BOJ");
        CITY_IATA.put("Белград", "BEG");           CITY_IATA.put("Belgrade", "BEG");
        CITY_IATA.put("Загреб", "ZAG");            CITY_IATA.put("Zagreb", "ZAG");
        CITY_IATA.put("Сплит", "SPU");             CITY_IATA.put("Split", "SPU");
        CITY_IATA.put("Дубровник", "DBV");         CITY_IATA.put("Dubrovnik", "DBV");
        CITY_IATA.put("Сараево", "SJJ");           CITY_IATA.put("Sarajevo", "SJJ");
        CITY_IATA.put("Подгорица", "TGD");         CITY_IATA.put("Podgorica", "TGD");
        CITY_IATA.put("Тиват", "TIV");             CITY_IATA.put("Tivat", "TIV");
        CITY_IATA.put("Скопье", "SKP");            CITY_IATA.put("Skopje", "SKP");
        CITY_IATA.put("Тирана", "TIA");            CITY_IATA.put("Tirana", "TIA");
        CITY_IATA.put("Афины", "ATH");             CITY_IATA.put("Athens", "ATH");
        CITY_IATA.put("Салоники", "SKG");          CITY_IATA.put("Thessaloniki", "SKG");
        CITY_IATA.put("Ираклион", "HER");          CITY_IATA.put("Heraklion", "HER");
        CITY_IATA.put("Родос", "RHO");             CITY_IATA.put("Rhodes", "RHO");
        CITY_IATA.put("Корфу", "CFU");             CITY_IATA.put("Corfu", "CFU");
        CITY_IATA.put("Вильнюс", "VNO");           CITY_IATA.put("Vilnius", "VNO");
        CITY_IATA.put("Рига", "RIX");              CITY_IATA.put("Riga", "RIX");
        CITY_IATA.put("Таллин", "TLL");            CITY_IATA.put("Tallinn", "TLL");
        CITY_IATA.put("Люксембург", "LUX");        CITY_IATA.put("Luxembourg", "LUX");
        CITY_IATA.put("Валлетта", "MLA");          CITY_IATA.put("Malta", "MLA");
        CITY_IATA.put("Никосия", "LCA");           CITY_IATA.put("Larnaca", "LCA");
        CITY_IATA.put("Пафос", "PFO");             CITY_IATA.put("Paphos", "PFO");

        // ═══ СЕВЕРНАЯ АМЕРИКА ═══
        CITY_IATA.put("Нью-Йорк", "JFK");          CITY_IATA.put("New York", "JFK");
        CITY_IATA.put("Ньюарк", "EWR");            CITY_IATA.put("Newark", "EWR");
        CITY_IATA.put("Лос-Анджелес", "LAX");      CITY_IATA.put("Los Angeles", "LAX");
        CITY_IATA.put("Чикаго", "ORD");            CITY_IATA.put("Chicago", "ORD");
        CITY_IATA.put("Майами", "MIA");            CITY_IATA.put("Miami", "MIA");
        CITY_IATA.put("Орландо", "MCO");           CITY_IATA.put("Orlando", "MCO");
        CITY_IATA.put("Лас-Вегас", "LAS");         CITY_IATA.put("Las Vegas", "LAS");
        CITY_IATA.put("Сан-Франциско", "SFO");     CITY_IATA.put("San Francisco", "SFO");
        CITY_IATA.put("Сиэтл", "SEA");             CITY_IATA.put("Seattle", "SEA");
        CITY_IATA.put("Бостон", "BOS");            CITY_IATA.put("Boston", "BOS");
        CITY_IATA.put("Вашингтон", "IAD");         CITY_IATA.put("Washington", "IAD");
        CITY_IATA.put("Атланта", "ATL");           CITY_IATA.put("Atlanta", "ATL");
        CITY_IATA.put("Даллас", "DFW");            CITY_IATA.put("Dallas", "DFW");
        CITY_IATA.put("Хьюстон", "IAH");           CITY_IATA.put("Houston", "IAH");
        CITY_IATA.put("Феникс", "PHX");            CITY_IATA.put("Phoenix", "PHX");
        CITY_IATA.put("Денвер", "DEN");            CITY_IATA.put("Denver", "DEN");
        CITY_IATA.put("Миннеаполис", "MSP");       CITY_IATA.put("Minneapolis", "MSP");
        CITY_IATA.put("Портленд", "PDX");          CITY_IATA.put("Portland", "PDX");
        CITY_IATA.put("Сан-Диего", "SAN");         CITY_IATA.put("San Diego", "SAN");
        CITY_IATA.put("Нэшвилл", "BNA");           CITY_IATA.put("Nashville", "BNA");
        CITY_IATA.put("Новый Орлеан", "MSY");      CITY_IATA.put("New Orleans", "MSY");
        CITY_IATA.put("Гонолулу", "HNL");          CITY_IATA.put("Honolulu", "HNL");
        CITY_IATA.put("Анкоридж", "ANC");          CITY_IATA.put("Anchorage", "ANC");
        CITY_IATA.put("Торонто", "YYZ");           CITY_IATA.put("Toronto", "YYZ");
        CITY_IATA.put("Ванкувер", "YVR");          CITY_IATA.put("Vancouver", "YVR");
        CITY_IATA.put("Монреаль", "YUL");          CITY_IATA.put("Montreal", "YUL");
        CITY_IATA.put("Калгари", "YYC");           CITY_IATA.put("Calgary", "YYC");
        CITY_IATA.put("Мехико", "MEX");            CITY_IATA.put("Mexico City", "MEX");
        CITY_IATA.put("Канкун", "CUN");            CITY_IATA.put("Cancun", "CUN");
        CITY_IATA.put("Гвадалахара", "GDL");       CITY_IATA.put("Guadalajara", "GDL");

        // ═══ ЦЕНТРАЛЬНАЯ И ЮЖНАЯ АМЕРИКА ═══
        CITY_IATA.put("Гавана", "HAV");            CITY_IATA.put("Havana", "HAV");
        CITY_IATA.put("Панама", "PTY");            CITY_IATA.put("Panama City", "PTY");
        CITY_IATA.put("Богота", "BOG");            CITY_IATA.put("Bogota", "BOG");
        CITY_IATA.put("Медельин", "MDE");          CITY_IATA.put("Medellin", "MDE");
        CITY_IATA.put("Картахена", "CTG");         CITY_IATA.put("Cartagena", "CTG");
        CITY_IATA.put("Кито", "UIO");              CITY_IATA.put("Quito", "UIO");
        CITY_IATA.put("Гуаякиль", "GYE");          CITY_IATA.put("Guayaquil", "GYE");
        CITY_IATA.put("Лима", "LIM");              CITY_IATA.put("Lima", "LIM");
        CITY_IATA.put("Куско", "CUZ");             CITY_IATA.put("Cusco", "CUZ");
        CITY_IATA.put("Ла-Пас", "LPB");            CITY_IATA.put("La Paz", "LPB");
        CITY_IATA.put("Сантьяго", "SCL");          CITY_IATA.put("Santiago", "SCL");
        CITY_IATA.put("Буэнос-Айрес", "EZE");      CITY_IATA.put("Buenos Aires", "EZE");
        CITY_IATA.put("Монтевидео", "MVD");        CITY_IATA.put("Montevideo", "MVD");
        CITY_IATA.put("Сан-Паулу", "GRU");         CITY_IATA.put("Sao Paulo", "GRU");
        CITY_IATA.put("Рио-де-Жанейро", "GIG");    CITY_IATA.put("Rio de Janeiro", "GIG");
        CITY_IATA.put("Бразилиа", "BSB");          CITY_IATA.put("Brasilia", "BSB");
        CITY_IATA.put("Каракас", "CCS");           CITY_IATA.put("Caracas", "CCS");

        // ═══ ОКЕАНИЯ ═══
        CITY_IATA.put("Сидней", "SYD");            CITY_IATA.put("Sydney", "SYD");
        CITY_IATA.put("Мельбурн", "MEL");          CITY_IATA.put("Melbourne", "MEL");
        CITY_IATA.put("Брисбен", "BNE");           CITY_IATA.put("Brisbane", "BNE");
        CITY_IATA.put("Перт", "PER");              CITY_IATA.put("Perth", "PER");
        CITY_IATA.put("Аделаида", "ADL");          CITY_IATA.put("Adelaide", "ADL");
        CITY_IATA.put("Окленд", "AKL");            CITY_IATA.put("Auckland", "AKL");
        CITY_IATA.put("Крайстчёрч", "CHC");        CITY_IATA.put("Christchurch", "CHC");
        CITY_IATA.put("Веллингтон", "WLG");        CITY_IATA.put("Wellington", "WLG");
        CITY_IATA.put("Нади", "NAN");              CITY_IATA.put("Nadi", "NAN");
        CITY_IATA.put("Папеэте", "PPT");           CITY_IATA.put("Papeete", "PPT");
    }

    private AutoCompleteTextView etFrom, etTo;
    private TextView tvDateDepart, tvDateReturn, tvPassengers, tvClass;
    private Button btnSearch;
    private ProgressBar progressBar;
    private int passengerCount = 1;
    private String selectedClass = "economy";
    private String departDate = "";
    private String returnDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_tickets);

        ImageButton btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) btnBack.setOnClickListener(v -> finish());

        etFrom = findViewById(R.id.et_from);
        etTo   = findViewById(R.id.et_to);
        setupCityAutocomplete(etFrom);
        setupCityAutocomplete(etTo);

        ImageButton btnSwap = findViewById(R.id.btn_swap);
        btnSwap.setOnClickListener(v -> {
            String tmp = etFrom.getText().toString();
            etFrom.setText(etTo.getText().toString());
            etTo.setText(tmp);
        });

        ImageButton btnClearTo = findViewById(R.id.btn_clear_to);
        btnClearTo.setOnClickListener(v -> etTo.setText(""));

        tvDateDepart = findViewById(R.id.tv_date_depart);
        tvDateReturn = findViewById(R.id.tv_date_return);
        tvDateDepart.setOnClickListener(v -> showDatePicker(true));
        tvDateReturn.setOnClickListener(v -> showDatePicker(false));

        tvPassengers = findViewById(R.id.tv_passengers);
        tvClass      = findViewById(R.id.tv_class);

        findViewById(R.id.btn_minus).setOnClickListener(v -> {
            if (passengerCount > 1) { passengerCount--; updatePassengersLabel(); }
        });
        findViewById(R.id.btn_plus).setOnClickListener(v -> {
            if (passengerCount < 9) { passengerCount++; updatePassengersLabel(); }
        });

        tvClass.setOnClickListener(v -> showClassPicker());

        btnSearch   = findViewById(R.id.btn_search_flights);
        progressBar = findViewById(R.id.progress_bar);
        btnSearch.setOnClickListener(v -> searchFlights());
    }

    private void setupCityAutocomplete(AutoCompleteTextView actv) {
        List<String> names = new ArrayList<>(CITY_IATA.keySet());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_dropdown_item_1line, names);
        actv.setAdapter(adapter);
        actv.setThreshold(1);
    }

    private String getIata(String input) {
        if (input == null || input.isEmpty()) return null;
        String code = CITY_IATA.get(input.trim());
        if (code != null) return code;
        String upper = input.trim().toUpperCase();
        if (upper.length() == 3 && upper.matches("[A-Z]{3}")) return upper;
        for (Map.Entry<String, String> e : CITY_IATA.entrySet())
            if (e.getKey().equalsIgnoreCase(input.trim())) return e.getValue();
        return null;
    }

    private void showDatePicker(boolean isDepart) {
        Calendar cal = Calendar.getInstance();
        new DatePickerDialog(this, R.style.DarkDatePicker,
                (view, year, month, day) -> {
                    String fmt  = String.format(Locale.US, "%04d-%02d-%02d", year, month+1, day);
                    String disp = String.format(Locale.US, "%02d.%02d.%04d", day, month+1, year);
                    if (isDepart) { departDate = fmt; tvDateDepart.setText("✈ " + disp); }
                    else          { returnDate  = fmt; tvDateReturn.setText("↩ " + disp); }
                },
                cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    private void updatePassengersLabel() {
        String cls = selectedClass.equals("business") ? "бизнес"
                : selectedClass.equals("first") ? "первый" : "эконом";
        tvPassengers.setText("👤 " + passengerCount + ", " + cls);
    }

    private void showClassPicker() {
        String[] labels = {"Эконом", "Бизнес", "Первый класс"};
        String[] values = {"economy", "business", "first"};
        new android.app.AlertDialog.Builder(this, R.style.DarkAlertDialog)
                .setTitle("Класс обслуживания")
                .setItems(labels, (d, which) -> {
                    selectedClass = values[which];
                    tvClass.setText("≡ " + labels[which]);
                    updatePassengersLabel();
                }).show();
    }

    private void searchFlights() {
        String fromText = etFrom.getText().toString().trim();
        String toText   = etTo.getText().toString().trim();

        if (fromText.isEmpty() || toText.isEmpty()) {
            Toast.makeText(this, "Введите города", Toast.LENGTH_SHORT).show(); return;
        }
        if (departDate.isEmpty()) {
            Toast.makeText(this, "Выберите дату вылета", Toast.LENGTH_SHORT).show(); return;
        }

        String fromIata = getIata(fromText);
        String toIata   = getIata(toText);

        if (fromIata == null) {
            Toast.makeText(this, "Не найден аэропорт: " + fromText, Toast.LENGTH_SHORT).show(); return;
        }
        if (toIata == null) {
            Toast.makeText(this, "Не найден аэропорт: " + toText, Toast.LENGTH_SHORT).show(); return;
        }

        btnSearch.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);

        try {
            JSONObject passenger = new JSONObject();
            passenger.put("type", "adult");

            JSONArray passengers = new JSONArray();
            for (int i = 0; i < passengerCount; i++) passengers.put(passenger);

            JSONObject originSlice = new JSONObject();
            originSlice.put("origin", fromIata);
            originSlice.put("destination", toIata);
            originSlice.put("departure_date", departDate);

            JSONArray slices = new JSONArray();
            slices.put(originSlice);

            if (!returnDate.isEmpty()) {
                JSONObject returnSlice = new JSONObject();
                returnSlice.put("origin", toIata);
                returnSlice.put("destination", fromIata);
                returnSlice.put("departure_date", returnDate);
                slices.put(returnSlice);
            }

            JSONObject requestData = new JSONObject();
            requestData.put("slices", slices);
            requestData.put("passengers", passengers);
            requestData.put("cabin_class", selectedClass);

            JSONObject body = new JSONObject();
            body.put("data", requestData);

            OkHttpClient client = new OkHttpClient();
            RequestBody reqBody = RequestBody.create(
                    body.toString(), MediaType.parse("application/json"));

            Request request = new Request.Builder()
                    .url("https://api.duffel.com/air/offer_requests?return_offers=true")
                    .addHeader("Authorization", DUFFEL_TOKEN)
                    .addHeader("Duffel-Version", "v2")
                    .addHeader("Accept", "application/json")
                    .addHeader("Content-Type", "application/json")
                    .post(reqBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        btnSearch.setEnabled(true);
                        Toast.makeText(BookTicketsActivity.this,
                                "Ошибка сети: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String json = response.body().string();
                    runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        btnSearch.setEnabled(true);
                        try {
                            android.util.Log.d("DUFFEL", "Code: " + response.code());
                            android.util.Log.d("DUFFEL", "Body: " + json);

                            Gson gson = new Gson();
                            DuffelOfferResponse resp = gson.fromJson(json, DuffelOfferResponse.class);
                            if (resp != null && resp.data != null && resp.data.offers != null
                                    && !resp.data.offers.isEmpty()) {
                                FlightSearchHolder.offers = resp.data.offers;
                                startActivity(new Intent(BookTicketsActivity.this,
                                        FlightResultsActivity.class));
                            } else {
                                Toast.makeText(BookTicketsActivity.this,
                                        "Рейсы не найдены. Code: " + response.code(),
                                        Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(BookTicketsActivity.this,
                                    "Ошибка: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });

        } catch (JSONException e) {
            progressBar.setVisibility(View.GONE);
            btnSearch.setEnabled(true);
            Toast.makeText(this, "Ошибка запроса", Toast.LENGTH_SHORT).show();
        }
    }
}