package com.chenzhang.mvi.data

import java.util.*

/**
 * This class mocks data repository; In reality, it should be a resource on network or database
 */
class DataRepository {

    private val recordings = mutableListOf<Recording>(
            Recording(id = asset10Id, title = asset10Title, recordingType = asset10Type, description = asset10Desc, recordingTime = asset10Date),
            Recording(id = asset1Id, title = asset1Title, recordingType = asset1Type, description = asset1Desc, recordingTime = asset1Date),
            Recording(id = asset2Id, title = asset2Title, recordingType = asset2Type, description = asset2Desc, recordingTime = asset2Date),
            Recording(id = asset3Id, title = asset3Title, recordingType = asset3Type, description = asset3Desc, recordingTime = asset3Date),
            Recording(id = asset4Id, title = asset4Title, recordingType = asset4Type, description = asset4Desc, recordingTime = asset4Date)
    )

    fun loadCurrentRecordings(): List<Recording> = recordings.toList()

    fun deleteReocrding(recording: Recording) {
        recordings.remove(recording)
    }

    fun getRecorderUsage() = recordings.size *12

    companion object {

        private val asset10Id = "M1000384402"
        private val asset10Title = "A Dog's Purpose"
        private val asset10Desc = "Reincarnated as different dogs over the course of five decades, a lovable and devoted canine (Josh Gad) keeps reuniting with the original owner who cared for it as a golden retriever puppy."
        private val asset10Type = RecordingType.MOVIE
        private val asset10Date = Date(1520650800000)

        private val asset1Id = "M1000384402"
        private val asset1Title = "Star Wars: The Force Awakens"
        private val asset1Desc = "Thirty years after the defeat of the Galactic Empire, Han Solo (Harrison Ford) and his young allies face a new threat from the evil Kylo Ren (Adam Driver) and the First Order."
        private val asset1Type = RecordingType.MOVIE
        private val asset1Date = Date(1518440400000)

        private val asset2Id = "T1000984322"
        private val asset2Title = "Fix Upper"
        private val asset2Desc = "Musician Mike Herrera and his wife, Holli, move from Washington to Waco, Texas, so they can be closer to Holli's family; they need a house with plenty of space for their two children and a large music studio for Mike."
        private val asset2Type = RecordingType.TV
        private val asset2Date = Date(1516806000000)

        private val asset3Id = "V1000984322"
        private val asset3Title = "Pop and Country MV"
        private val asset3Desc = "Favorite country and pop stars for an audience that is a little bit country and a little bit pop includes Florida Georgia Line, Selena Gomez, Bruno Mars, and Hunter Hayes."
        private val asset3Type = RecordingType.MV
        private val asset3Date = Date(1516654800000)

        private val asset4Id = "S1000984322"
        private val asset4Title = "U.S.Open 2017 Lookback"
        private val asset4Desc = "Rafael Nadal completed a dominant US Open performance Sunday, defeating Kevin Anderson 6-3, 6-3, 6-4. Nadal caps off an extraordinary 2017 season as the No. 1 player in the world, in which he won the French and US Opens and made the Australian Open final against Roger Federer."
        private val asset4Type = RecordingType.SPORT
        private val asset4Date = Date(1515672000000)
    }
}
