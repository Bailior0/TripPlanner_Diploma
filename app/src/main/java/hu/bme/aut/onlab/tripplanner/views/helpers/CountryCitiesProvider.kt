package hu.bme.aut.onlab.tripplanner.views.helpers

object CountryCitiesProvider {

    val countriesWithCities = mapOf(

        // 🇭🇺 Magyarország
        "Magyarország" to listOf("Budapest", "Debrecen", "Szeged", "Pécs", "Győr"),

        // 🇦🇱 Albánia
        "Albánia" to listOf("Tirana", "Durres", "Shkoder"),

        // 🇩🇿 Algéria
        "Algéria" to listOf("Algír", "Oran"),

        // 🇦🇩 Andorra
        "Andorra" to listOf("Andorra la Vella"),

        // 🇦🇴 Angola
        "Angola" to listOf("Luanda"),

        // 🇦🇬 Antigua és Barbuda
        "Antigua és Barbuda" to listOf("Saint John’s"),

        // 🇦🇷 Argentína
        "Argentína" to listOf("Buenos Aires", "Córdoba", "Mendoza", "Bariloche"),

        // 🇦🇲 Örményország
        "Örményország" to listOf("Jereván"),

        // 🇦🇺 Ausztrália
        "Ausztrália" to listOf("Canberra", "Sydney", "Melbourne", "Brisbane", "Perth"),

        // 🇦🇹 Ausztria
        "Ausztria" to listOf("Bécs", "Salzburg", "Graz", "Innsbruck"),

        // 🇦🇿 Azerbajdzsán
        "Azerbajdzsán" to listOf("Baku"),

        // 🇧🇸 Bahama-szigetek
        "Bahama-szigetek" to listOf("Nassau"),

        // 🇧🇭 Bahrein
        "Bahrein" to listOf("Manáma"),

        // 🇧🇩 Banglades
        "Banglades" to listOf("Dakha"),

        // 🇧🇧 Barbados
        "Barbados" to listOf("Bridgetown"),

        // 🇧🇾 Fehéroroszország
        "Fehéroroszország" to listOf("Minszk"),

        // 🇧🇪 Belgium
        "Belgium" to listOf("Brüsszel", "Antwerpen", "Brugge", "Gent"),

        // 🇧🇿 Belize
        "Belize" to listOf("Belmopan"),

        // 🇧🇯 Benin
        "Benin" to listOf("Porto-Novo", "Cotonou"),

        // 🇧🇹 Bhután
        "Bhután" to listOf("Thimphu"),

        // 🇧🇴 Bolívia
        "Bolívia" to listOf("La Paz", "Sucre"),

        // 🇧🇦 Bosznia-Hercegovina
        "Bosznia-Hercegovina" to listOf("Szarajevó", "Mostar", "Banja Luka"),

        // 🇧🇼 Botswana
        "Botswana" to listOf("Gaborone"),

        // 🇧🇷 Brazília
        "Brazília" to listOf("Brasília", "Rio de Janeiro", "São Paulo", "Salvador"),

        // 🇧🇳 Brunei
        "Brunei" to listOf("Bandar Seri Begawan"),

        // 🇧🇬 Bulgária
        "Bulgária" to listOf("Szófia", "Plovdiv", "Várna", "Burgasz"),

        // 🇧🇫 Burkina Faso
        "Burkina Faso" to listOf("Ouagadougou"),

        // 🇧🇮 Burundi
        "Burundi" to listOf("Gitega"),

        // 🇰🇭 Kambodzsa
        "Kambodzsa" to listOf("Phnom Penh", "Siem Reap"),

        // 🇨🇲 Kamerun
        "Kamerun" to listOf("Yaoundé"),

        // 🇨🇦 Kanada
        "Kanada" to listOf("Ottawa", "Toronto", "Vancouver", "Montréal"),

        "Zöld-foki Köztársaság" to listOf("Praia"),
        "Közép-afrikai Köztársaság" to listOf("Bangui"),
        "Csád" to listOf("N'Djamena"),
        "Chile" to listOf("Santiago", "Valparaíso"),
        "Kína" to listOf("Peking", "Sanghaj", "Shenzhen", "Kanton", "Xi’an"),
        "Kolumbia" to listOf("Bogotá", "Medellín", "Cartagena"),
        "Comore-szigetek" to listOf("Moroni"),
        "Kongói DK" to listOf("Kinshasa"),
        "Kongói Köztársaság" to listOf("Brazzaville"),
        "Costa Rica" to listOf("San José"),
        "Elefántcsontpart" to listOf("Yamoussoukro", "Abidjan"),
        "Horvátország" to listOf("Zágráb", "Split", "Dubrovnik", "Zadar"),
        "Kuba" to listOf("Havanna", "Varadero"),
        "Ciprus" to listOf("Nicosia", "Larnaka", "Páfosz"),
        "Csehország" to listOf("Prága", "Brno", "Ostrava"),
        "Dánia" to listOf("Koppenhága", "Aarhus"),
        "Dzsibuti" to listOf("Dzsibuti"),
        "Dominika" to listOf("Roseau"),
        "Dominikai Köztársaság" to listOf("Santo Domingo", "Punta Cana"),
        "Ecuador" to listOf("Quito", "Guayaquil"),
        "Egyiptom" to listOf("Kairó", "Hurghada", "Sharm el-Sheikh", "Luxor"),
        "El Salvador" to listOf("San Salvador"),
        "Egyenlítői-Guinea" to listOf("Malabo"),
        "Eritrea" to listOf("Aszmara"),
        "Észtország" to listOf("Tallinn", "Tartu"),
        "Etiópia" to listOf("Addisz-Abeba"),
        "Fidzsi-szigetek" to listOf("Suva"),
        "Finnország" to listOf("Helsinki", "Tampere"),
        "Franciaország" to listOf("Párizs", "Lyon", "Marseille", "Nizza"),
        "Gabon" to listOf("Libreville"),
        "Gambia" to listOf("Banjul"),
        "Grúzia" to listOf("Tbiliszi", "Batumi"),
        "Németország" to listOf("Berlin", "München", "Hamburg", "Frankfurt"),
        "Ghána" to listOf("Accra"),
        "Görögország" to listOf("Athén", "Thesszaloniki", "Santorini", "Kréta"),
        "Grenada" to listOf("Saint George’s"),
        "Guatemala" to listOf("Guatemala City"),
        "Guinea" to listOf("Conakry"),
        "Guinea-Bissau" to listOf("Bissau"),
        "Guyana" to listOf("Georgetown"),
        "Haiti" to listOf("Port-au-Prince"),
        "Honduras" to listOf("Tegucigalpa"),
        "Hongkong" to listOf("Hongkong"),
        "Izland" to listOf("Reykjavík"),
        "India" to listOf("Újdelhi", "Mumbai", "Goa", "Bengaluru"),
        "Indonézia" to listOf("Jakarta", "Bali", "Yogyakarta"),
        "Irán" to listOf("Teherán"),
        "Irak" to listOf("Bagdad"),
        "Írország" to listOf("Dublin", "Cork", "Galway"),
        "Izrael" to listOf("Jeruzsálem", "Tel-Aviv", "Eilat"),
        "Olaszország" to listOf("Róma", "Milánó", "Velence", "Firenze", "Nápoly"),
        "Jamaica" to listOf("Kingston", "Montego Bay"),
        "Japán" to listOf("Tokió", "Kiotó", "Osaka", "Hiroshima"),
        "Jordánia" to listOf("Ammán", "Akaba", "Petra"),
        "Kazahsztán" to listOf("Asztana", "Almati"),
        "Kenya" to listOf("Nairobi", "Mombasa"),
        "Kiribati" to listOf("Tarawa"),
        "Kuwait" to listOf("Kuvaitváros"),
        "Kirgizisztán" to listOf("Biskek"),
        "Laosz" to listOf("Vientián"),
        "Lettország" to listOf("Riga"),
        "Libanon" to listOf("Bejrút"),
        "Lesotho" to listOf("Maseru"),
        "Libéria" to listOf("Monrovia"),
        "Líbia" to listOf("Tripoli"),
        "Liechtenstein" to listOf("Vaduz"),
        "Litvánia" to listOf("Vilnius", "Kaunas"),
        "Luxemburg" to listOf("Luxembourg"),
        "Macedónia" to listOf("Szkopje"),
        "Madagaszkár" to listOf("Antananarivo"),
        "Malawi" to listOf("Lilongwe"),
        "Malajzia" to listOf("Kuala Lumpur", "Penang", "Langkawi"),
        "Maldív-szigetek" to listOf("Malé"),
        "Mali" to listOf("Bamako"),
        "Málta" to listOf("Valletta"),
        "Marshall-szigetek" to listOf("Majuro"),
        "Mauritánia" to listOf("Nouakchott"),
        "Mauritius" to listOf("Port Louis"),
        "Mexikó" to listOf("Mexikóváros", "Cancún", "Tulum"),
        "Mikronézia" to listOf("Palikir"),
        "Moldova" to listOf("Chișinău"),
        "Monaco" to listOf("Monaco"),
        "Mongólia" to listOf("Ulánbátor"),
        "Montenegró" to listOf("Podgorica", "Kotor", "Budva"),
        "Marokkó" to listOf("Rabat", "Marrákes", "Casablanca"),
        "Mozambik" to listOf("Maputo"),
        "Mianmar" to listOf("Naypyidaw"),
        "Namíbia" to listOf("Windhoek"),
        "Nepál" to listOf("Katmandu"),
        "Hollandia" to listOf("Amszterdam", "Rotterdam", "Hága"),
        "Új-Zéland" to listOf("Wellington", "Auckland", "Queenstown"),
        "Nicaragua" to listOf("Managua"),
        "Niger" to listOf("Niamey"),
        "Nigéria" to listOf("Abuja", "Lagos"),
        "Észak-Korea" to listOf("Phenjan"),
        "Norvégia" to listOf("Oslo", "Bergen"),
        "Omán" to listOf("Maszkat"),
        "Pakisztán" to listOf("Iszlámábád", "Karacsi", "Lahore"),
        "Palausz" to listOf("Ngerulmud"),
        "Panama" to listOf("Panama City"),
        "Pápua Új-Guinea" to listOf("Port Moresby"),
        "Paraguay" to listOf("Asunción"),
        "Peru" to listOf("Lima", "Cusco"),
        "Fülöp-szigetek" to listOf("Manila", "Cebu"),
        "Lengyelország" to listOf("Varsó", "Krakkó", "Gdansk"),
        "Portugália" to listOf("Lisszabon", "Porto", "Faro"),
        "Katar" to listOf("Doha"),
        "Dél-Korea" to listOf("Szöul", "Busan"),
        "Románia" to listOf("Bukarest", "Kolozsvár", "Brassó"),
        "Oroszország" to listOf("Moszkva", "Szentpétervár"),
        "Ruanda" to listOf("Kigali"),
        "Saint Kitts és Nevis" to listOf("Basseterre"),
        "Saint Lucia" to listOf("Castries"),
        "Saint Vincent és Grenadine-szigetek" to listOf("Kingstown"),
        "Szamoa" to listOf("Apia"),
        "San Marino" to listOf("San Marino"),
        "Szaúd-Arábia" to listOf("Rijád", "Dzsidda"),
        "Szenegál" to listOf("Dakar"),
        "Szerbia" to listOf("Belgrád", "Újvidék"),
        "Seychelle-szigetek" to listOf("Victoria"),
        "Sierra Leone" to listOf("Freetown"),
        "Szingapúr" to listOf("Szingapúr"),
        "Szlovákia" to listOf("Pozsony", "Kassa"),
        "Szlovénia" to listOf("Ljubljana", "Bled"),
        "Salamon-szigetek" to listOf("Honiara"),
        "Szomália" to listOf("Mogadishu"),
        "Dél-afrikai Köztársaság" to listOf("Pretoria", "Fokváros", "Johannesburg"),
        "Dél-Szudán" to listOf("Juba"),
        "Spanyolország" to listOf("Madrid", "Barcelona", "Valencia"),
        "Srí Lanka" to listOf("Sri Jayawardenepura Kotte", "Colombo"),
        "Szudán" to listOf("Kartúm"),
        "Suriname" to listOf("Paramaribo"),
        "Svédország" to listOf("Stockholm", "Göteborg", "Malmö"),
        "Svájc" to listOf("Bern", "Zürich", "Genf"),
        "Szíria" to listOf("Damaszkusz"),
        "Tajvan" to listOf("Tajpej"),
        "Tádzsikisztán" to listOf("Dusanbe"),
        "Tanzánia" to listOf("Dodoma", "Zanzibár", "Dar es Salaam"),
        "Thaiföld" to listOf("Bangkok", "Phuket", "Chiang Mai"),
        "Togo" to listOf("Lomé"),
        "Tonga" to listOf("Nuku’alofa"),
        "Trinidad és Tobago" to listOf("Port of Spain"),
        "Tunézia" to listOf("Tunisz", "Sousse", "Monastir"),
        "Törökország" to listOf("Ankara", "Isztambul", "Izmir", "Antalya"),
        "Türkmenisztán" to listOf("Asgabat"),
        "Tuvalu" to listOf("Funafuti"),
        "Uganda" to listOf("Kampala"),
        "Ukrajna" to listOf("Kijev", "Lviv", "Odessza"),
        "Egyesült Arab Emírségek" to listOf("Abu Dhabi", "Dubaj", "Sharjah"),
        "Egyesült Királyság" to listOf("London", "Manchester", "Edinburgh"),
        "Egyesült Államok" to listOf("Washington D.C.", "New York", "Los Angeles", "Chicago", "Miami", "Boston", "San Francisco"),
        "Uruguay" to listOf("Montevideo"),
        "Üzbegisztán" to listOf("Taskent"),
        "Vanuatu" to listOf("Port Vila"),
        "Vatikán" to listOf("Vatikánváros"),
        "Venezuela" to listOf("Caracas"),
        "Vietnam" to listOf("Hanoi", "Ho Si Minh-város"),
        "Jemen" to listOf("Szanaa"),
        "Zambia" to listOf("Lusaka"),
        "Zimbabwe" to listOf("Harare")
    )
}
