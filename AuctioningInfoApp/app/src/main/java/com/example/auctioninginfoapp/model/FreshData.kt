package com.example.auctioninginfoapp.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

data class ResultCode(
    val code: String, //메세지코드(INFO-000, INFO-100)
    val message: String
)

/** 응답 결과(Json)를 FreshData 형태로 파싱하기 위한 DTO 클래스
 * - DTO(Data Transfer Object): 계층 간 데이터 교환을 위해 사용하는 객체
 */
data class FreshWrapper(
    /** 데이터 요청 성공시 리턴값 */
    @Json(name = "Grid_20160624000000000348_1")
    var list: ResultMeta?,

    /** 데이터 요청 실패시 리턴값 */
    @Json(name = "result")
    var errorCode: ResultCode?
)

/** 응답 결과 클래스
 * -totalCnt(전체건수), startRow(시작 Row), endRow(끝Row), resultCode(응답코드): 참고용 메타정보
 * -row: 응답 결과(List 형태의 FreshData)
 */
data class ResultMeta(
    @Json(name = "totalCnt")
    var totalCnt: Int? = null,
    @Json(name = "startRow")
    var startRow: Int? = null,
    @Json(name = "endRow")
    var endRow: Int? = null,
    @Json(name = "result")
    var result: ResultCode?,
    @Json(name = "row")
    var row: List<FreshData>

)


/** SaveItem Entity
 * - 검색 결과 DB 저장 목록 리스트
 * saveItem(id=1, saveTitle=2023-05-30 사과 검색결과)
 */

@Entity(tableName = "SaveItem")
data class SaveItem(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    var saveTitle: String
)

/** Fresh Entity
 * 검색 결과 DB 저장
 */
@Entity(tableName = "Fresh",
    foreignKeys = [
        ForeignKey(
        /** 참조한 부모 Entity(SaveItem)의 parentColumns(id)이 삭제될 경우
         * 이를 참조하는 자식 Entity(Fresh) 의 childColumns(saveId)도 함께 삭제
         */
        onDelete = ForeignKey.CASCADE,
        entity = SaveItem::class, //외래키가 참조할 부모 Entity
        parentColumns = arrayOf("id"), //참조할 부모 Entity의 Key(필드명)
        childColumns = arrayOf("saveId") //참조한 부모의 Key를 저장할 자식 Entity의 Key
    )]
)

data class FreshData(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    var saveId: Long? = null,
    /** Entity 필드에 JSON 인코딩 방식 정의(Moshi-kotlin)
     * -JSON 데이터의 각 오브젝트({"ROW_NUM":1,...,"PRDLST_NM":"사과",...","SPCIES_NM":"후지",...})의
     * "name":"value"를 Entity의 각 필드의 값으로 변환
     */
    @Json(name="PRDLST_NM") //품목명
    var mname: String,
    @Json(name="SPCIES_NM") //품종명
    var sname: String,
    @Json(name="STNDRD") //규격
    var uname: String,
    @Json(name="GRAD") //등급
    var grade: String,
    @Json(name="CPR_NM") //법인명
    var cprName: String,
    @Json(name="MXMM_AMT") //최대가
    var maxPrice: String,
    @Json(name="MUMM_AMT") //최소가
    var minPrice: String,
    @Json(name="AVRG_AMT") //평균가
    var avgPrice: String,
    @Json(name="CNTS") //경매건수
    var tradeAmt: String
)
