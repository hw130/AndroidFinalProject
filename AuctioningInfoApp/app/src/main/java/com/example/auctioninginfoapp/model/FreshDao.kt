package com.example.auctioninginfoapp.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import javax.sql.DataSource

/** 데이터베이스 Access를 위한 Dao 인터페이스 */
@Dao
interface FreshDao {
    /** Fresh Entity에 검색한 경락가격정보 저장 */
    @Insert
    fun insertFresh(freshData: List<FreshData>)

    /** Fresh Entity에 저장할 경락가격정보의 목록(id, saveTitle)을 SaveItem Entity에 저장 */
    @Insert
    fun insertSave(saveItem: SaveItem): Long

    /** DB(SaveItem)에서 데이터를 쿼리하여 PagingSource(DataSource) 객체로 변환
     * -DataSource.Factory<key, value>
     *     .key: 데이터 식별자(페이징에 쓰일 식별자로 정수 기반의 page 값인 Int)
     *     .value: 데이터의 타입(객체 타입)
     */
    @Query("SELECT * FROM SaveItem")
    fun loadSaveItems(): androidx.paging.DataSource.Factory<Int, SaveItem>

    /** DB(Fresh)ㅇㅔ서 데이터를 쿼리하여 PagingSource(DataSource) 객체로 반환 */
    @Query("SELECT * FROM Fresh WHERE saveId = :saveId")
    fun loadFreshData(saveId: Long): androidx.paging.DataSource.Factory<Int, FreshData>

    /** DELETE Query */
    @Query("DELETE FROM SaveItem WHERE id = :saveId")
    fun deleteSaveData(saveId: Long)


}