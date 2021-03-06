package ClashDetectionKotlin

import Timetable.Timetable

class KotlinDetector(TimetableObj: Timetable) {

    private var Timetable: Timetable

    init {
        this.Timetable = TimetableObj
    }

    /**
     * @return MutableList<List<Int>> Element of the list [0 -> year table, 1->term table number, 2->week table number, 3->Time table number, 4-> WeekDay table number]
     */
    fun getTableSlots(clashes: Set<Int>): MutableList<List<Int>> {

        val slots : MutableList<List<Int>> = arrayListOf()

        val doubleTimeSlotToInt = HashMap<Double, Int>()
        var counter = 9.0
        var counter_ = 0
        while (counter < 21.5) {
            doubleTimeSlotToInt[counter] = counter_
            counter += 0.5
            counter_++
        }

        for (year in this.Timetable.table){
            for (term in year.value.terms){
                for (week in term.value.weeks){
                    for (day in week.value.days){
                        for (slot in day.value.TimeSlot){
                            slot.value?.let {
                                if (it.size > 1){
                                    for (activity in it){
                                        //println("$it --- ${activity.ID}")
                                        if (activity.ID in clashes){
                                            slots.add(listOf(year.key, term.key, week.key, doubleTimeSlotToInt.get(slot.key), day.key + 1) as List<Int>)
                                        }
                                    }
                                }
                                else{
                                    if (it[0].ID in clashes){
                                        slots.add(listOf(year.key, term.key, week.key, doubleTimeSlotToInt.get(slot.key), day.key + 1) as List<Int>)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return slots

    }

    fun detect(): MutableList<List<Int>> {

        val clashes : MutableList<Int> = arrayListOf()

        for (year in this.Timetable.table){
            for (term in year.value.terms){
                for (week in term.value.weeks){
                    for (day in week.value.days){
                        for (slot in day.value.TimeSlot){
                            slot.value?.let {
                                if (it.size > 1){
                                    var compulsory = false
                                    for (activity in it){
                                        //println("$it --- ${activity.ID}")
                                        //clashes.add(activity.ID)
                                        if (this.Timetable.modules[activity.Module]?.IsOptional == false){
                                            //clashes.add(activity.ID)
                                            compulsory = true
                                        }
                                    }
                                    if (compulsory){
                                        for (activity in it){
                                            clashes.add(activity.ID)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return getTableSlots(clashes.toSet())
    }
    
}