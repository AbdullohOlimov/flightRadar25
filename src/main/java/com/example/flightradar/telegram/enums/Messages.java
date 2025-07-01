package com.example.flightradar.telegram.enums;

import lombok.Getter;

@Getter
public enum Messages {
    WELCOME_TEXT("""
             🛫 <b>Salom, %s!</b>
            
            Men FlightWatcher botman. Samolyotlar holatini kuzatishda yordam beraman.
            
            <b>📋 Mavjud komandalar:</b>
            /flight [REYS] - Reys ma'lumotlarini olish
            /search [Aeroport] - Aeroport bo'yicha qidirish
            /help - Yordam
            
            <b>🔍 Misol:</b>
            <code>/flight UZ1234</code>
            <code>/search Tashkent</code>
            
            <i>Vaqt: %s</i>
            
            """),

    FLIGHT_INFO("""
            ✈️ <b>Reys Ma'lumotlari</b>
            
            🎫 <b>Reys:</b> %s
            🏢 <b>Aviakompaniya:</b> %s
            
            🛫 <b>Jo'nash:</b>
            📍 %s (%s)
            ⏰ %s
            
            🛬 <b>Qo'nish:</b>
            📍 %s (%s)  
            ⏰ %s
            
            📊 <b>Holat:</b> %s
            🌍 <b>So'nggi yangilanish:</b> %s
            """),

    FLIGHT_CORRECT_FORM("❗Iltimos, reys kodini kiriting. Masalan: <code>/flight UZ1234</code>"),
    FLIGHT_SEARCHING_ERROR("❗ <b>%s</b> reysi qidirishda xatolik yuz berdi. Iltimos, qayta urinib ko'ring."),
    AIRPORT_SEARCHING("❗ Iltimos, aeroport nomini kiriting. Masalan: <code>/search Tashkent</code>"),
    AIRPORT_INFO("""
                🔍 <b>%s</b> aeroporti bo'yicha qidiruv natijasi:
                
                📍 <b>Aeroport:</b> %s International Airport
                🛫 <b>Holat:</b> Faol
                ⏰ <b>Mahalliy vaqt:</b> %s
                
                💡 Aniq reys ma'lumotlari uchun /flight komandasi bilan reys kodini kiriting.
                """),
    AIRPORT_SEARCHING_ERR0R("❗ <b>%s</b> aeroporti qidirishda xatolik yuz berdi."),
    HELP_MESSAGE("""
            📚 <b>FlightWatcher Bot - Yordam</b>
            
            <b>🎯 Komandalar:</b>
            
            🔍 <code>/flight [REYS]</code>
            Reys ma'lumotlarini olish
            
            🏢 <code>/search [AEROPORT]</code>
            Aeroport bo'yicha qidirish
            
            📍 <code>/track [REYS]</code>
            Reysni kuzatishga qo'shish
            
            ❓ <code>/help</code>
            Ushbu yordam xabari
            
            <b>💡 Masalalar:</b>
            • <code>/flight UZ1234</code>
            • <code>/search Tashkent</code>
            • <code>/track HY456</code>
            
            <i>Savollar bo'lsa, /start tugmasini bosing.</i>
            """),
    UNKNOWN_MESSAGE("""
            ❗ <b>Noma'lum komanda</b>
            
            📋 Mavjud komandalar:
            /flight [REYS] - Reys ma'lumotlari
            /search [AEROPORT] - Aeroport qidirish
            /track [REYS] - Reys kuzatish
            /help - Yordam
            
            💡 <code>/start</code> ni bosing yoki to'g'ri komanda yuboring.
            """),

    FLIGHT_NOT_FOUND("Siz kiritgan reys ma'lumotlari topilmadi."),

    ERROR_MESSAGE("❗ Xatolik yuz berdi. Iltimos, qayta urinib ko'ring.");




    private final String text;

    private Messages(String s) {
        this.text = s;
    }

    public String format(Object...args){
        return String.format(this.text, args);
    }
}
