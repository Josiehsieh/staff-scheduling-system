package com.medical.calendar.data.local

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.medical.calendar.data.model.Calendar
import com.medical.calendar.data.model.CalendarEvent
import com.medical.calendar.data.model.CalendarType

@Database(
    entities = [CalendarEvent::class, Calendar::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class CalendarDatabase : RoomDatabase() {
    
    abstract fun calendarEventDao(): CalendarEventDao
    abstract fun calendarDao(): CalendarDao
    
    companion object {
        @Volatile
        private var INSTANCE: CalendarDatabase? = null
        
        fun getDatabase(context: Context): CalendarDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CalendarDatabase::class.java,
                    "calendar_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

class Converters {
    @androidx.room.TypeConverter
    fun fromCalendarType(value: CalendarType): String {
        return value.name
    }
    
    @androidx.room.TypeConverter
    fun toCalendarType(value: String): CalendarType {
        return CalendarType.valueOf(value)
    }
    
    @androidx.room.TypeConverter
    fun fromLocalDateTime(value: kotlinx.datetime.LocalDateTime): String {
        return value.toString()
    }
    
    @androidx.room.TypeConverter
    fun toLocalDateTime(value: String): kotlinx.datetime.LocalDateTime {
        return kotlinx.datetime.LocalDateTime.parse(value)
    }
} 