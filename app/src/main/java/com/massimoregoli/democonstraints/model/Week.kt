package com.massimoregoli.democonstraints.model


class Person(var name: String, var icon: String)

class Staff {
    var persons = mutableListOf<Person>()

    fun add(p: Person) {
        persons.add(p)
    }

    fun remove(p: Person) {
        persons.remove(p)
    }

    fun get(name: String): Person? {
        return persons.find { it.name == name }
    }

    fun isPresent(p: Person): Boolean {
        persons.forEach {
            if (it.name == p.name)
                return true
        }
        return false
    }

}

class Day(var day: Int) {
    var staff: Staff = Staff()

    fun add(p: Person) {
        staff.add(p)
    }
}

class Week(var persons: Staff) {

    var duty = List(7, init = {Day(it)})

    fun addPerson(day: Int, person: String) {
        duty[day].staff.add(persons.get(person)!!)
    }

    fun removePerson(day: Int, person: Person) {
        duty[day].staff.remove(person)
    }

    fun getPersons(): MutableList<Person> {
        val set = mutableSetOf<Person>()
        duty.forEach {
            it.staff.persons.forEach { person ->
                set.add(person)
            }
        }
        return set.toMutableList()
    }

    fun getDays(p: Person): MutableList<Int> {
        val ret = mutableListOf<Int>()
        duty.forEach {
            if (it.staff.isPresent(p)) {
                ret.add(it.day)
            }
        }
        return ret
    }
}


fun createSample(): Week {
    val p = Staff()
    p.add(Person("MR", ""))
    p.add(Person("FT", ""))
    p.add(Person("GG", ""))
    p.add(Person("MC", ""))
    p.add(Person("CP", ""))

    val w = Week(p)
    w.addPerson(0, "MR")
    w.addPerson(0, "FT")
    w.addPerson(0, "GG")
    w.addPerson(1, "GG")
    w.addPerson(1, "MC")
    w.addPerson(2, "CP")
    w.addPerson(2, "MR")
    w.addPerson(3, "FT")
    w.addPerson(3, "GG")
    w.addPerson(4, "MC")
    w.addPerson(4, "CP")
    w.addPerson(4, "MR")
    w.addPerson(5, "FT")
    w.addPerson(5, "GG")
    w.addPerson(5, "MC")
    w.addPerson(5, "CP")
    w.addPerson(6, "MR")
    w.addPerson(6, "CP")

    return w

}