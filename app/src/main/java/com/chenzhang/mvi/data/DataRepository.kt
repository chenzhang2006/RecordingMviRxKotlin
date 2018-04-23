package com.chenzhang.mvi.data

import java.util.*

/**
 * This class mocks data repository; In reality, it should be a resource on network or database
 */
class DataRepository {

    private val recordings = mutableListOf<Recording>(
            Recording(id = asset1Id, title = asset1Title, recordingType = asset1Type, description = asset1Desc, recordingTime = asset1Date),
            Recording(id = asset2Id, title = asset2Title, recordingType = asset2Type, description = asset2Desc, recordingTime = asset2Date),
            Recording(id = asset3Id, title = asset3Title, recordingType = asset3Type, description = asset3Desc, recordingTime = asset3Date),
            Recording(id = asset4Id, title = asset4Title, recordingType = asset4Type, description = asset4Desc, recordingTime = asset4Date),
            Recording(id = asset5Id, title = asset5Title, recordingType = asset5Type, description = asset5Desc, recordingTime = asset5Date),
            Recording(id = asset6Id, title = asset6Title, recordingType = asset6Type, description = asset6Desc, recordingTime = asset6Date)
    )

    fun loadCurrentRecordings(): List<Recording> = recordings.toList()

    fun deleteReocrding(recording: Recording) {
        recordings.remove(recording)
    }

    fun getRecorderUsage() = recordings.size * 12

    companion object {

        private val asset1Id = "M1000384402"
        private val asset1Title = "A Dog's Purpose"
        private val asset1Desc = "Reincarnated as different dogs over the course of five decades, a lovable and devoted canine (Josh Gad) keeps reuniting with the original owner who cared for it as a golden retriever puppy."
        private val asset1Type = RecordingType.MOVIE
        private val asset1Date = Date(1520650800000)

        private val asset2Id = "T1000876803"
        private val asset2Title = "Big Little Lies"
        private val asset2Desc = "After another fight, Celeste makes a bold move; before the school's fall fundraiser, Madeline deals with fallout from her past; Jane learns who has really been hurting Amabella at school."
        private val asset2Type = RecordingType.TV
        private val asset2Date = Date(1519425000000)

        private val asset3Id = "M1000382202"
        private val asset3Title = "Star Wars: The Force Awakens"
        private val asset3Desc = "Thirty years after the defeat of the Galactic Empire, Han Solo (Harrison Ford) and his young allies face a new threat from the evil Kylo Ren (Adam Driver) and the First Order."
        private val asset3Type = RecordingType.MOVIE
        private val asset3Date = Date(1518440400000)

        private val asset4Id = "T1000984320"
        private val asset4Title = "Fix Upper"
        private val asset4Desc = "Musician Mike Herrera and his wife, Holli, move from Washington to Waco, Texas, so they can be closer to Holli's family; they need a house with plenty of space for their two children and a large music studio for Mike."
        private val asset4Type = RecordingType.TV
        private val asset4Date = Date(1516806000000)

        private val asset5Id = "V1000984326"
        private val asset5Title = "Pop and Country MV"
        private val asset5Desc = "Favorite country and pop stars for an audience that is a little bit country and a little bit pop includes Florida Georgia Line, Selena Gomez, Bruno Mars, and Hunter Hayes."
        private val asset5Type = RecordingType.MV
        private val asset5Date = Date(1516654800000)

        private val asset6Id = "S1000984312"
        private val asset6Title = "U.S.Open 2017 Lookback"
        private val asset6Desc = "Rafael Nadal completed a dominant US Open performance Sunday, defeating Kevin Anderson 6-3, 6-3, 6-4. Nadal caps off an extraordinary 2017 season as the No. 1 player in the world, in which he won the French and US Opens and made the Australian Open final against Roger Federer."
        private val asset6Type = RecordingType.SPORT
        private val asset6Date = Date(1515672000000)
    }
}
