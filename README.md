# konkuk_major_intensive_project
건국대학교 전공심화프로젝트 (6주)

# 주요 목표 기능
1. 공공 수유실 및 화장실 찾기.
2. 길찾기.
3. 리뷰 기능.
   <br>

# 화면구성 

계정 생성, 로그인<br><br>

지도<br>
	- 카테고리 (필터) <br>
	- 누르면 => 길찾기 기능, 리뷰 정보<br><br>

리뷰 (스캎폴드)<br>
	- 딱 댓글 보여주듯이만,,, (kakao 지도 느낌)<br><br>

계정정보 (스캎폴드)<br>
	- 아이디 정보<br>
	- 즐겨찾기 <br>
	- 작성한 리뷰 글 찾기<br>
	- 장소 제안<br><br>

# 객체 정보
장소 data <br>
	- 장소 이름 :  string <br>
	- 장소 주소 : string <br>
	- (좌표 x,y) : (x, y) class 만들기 <br>
	- 장소 카테고리 : ENUM class 만들기 <br>
	- 평균 평점 : float <br>
	- 리뷰 갯수 :  int <br>
	- 장소 특징 : string <br>
	- 리뷰 리스트 : 리뷰 데이터의 list 형식	 <br><br>

리뷰 data <br>
	- 유저 아이디 : string <br>
	- 장소 이름 :  string <br>
	- 장소 카테고리 : ENUM class 만들기<br>
	- 컨텐츠 : string  <br>
	- 리뷰 점수 : float  <br>
	- 날짜 :  localdatetime : db : long <br>
	- 사진 1장만 :  string <br> <br>

계정 data
	- 아이디 : string <br> 
	- 비번 : string  <br>
	- 즐겨찾기 : 장소 data를 list로... <br><br>





# data모음
   ... 이후 진행 상황 확인 후, 추가 기능 구현.

   수유실 : https://sooyusil.com/home/39.htm

   흡연실 :
   은평구, 강남구, 도봉구, 강동구 남음
구로구(실외) (API) https://www.data.go.kr/data/15069274/fileData.do#tab-layer-openapi
마포구 (파일) https://www.data.go.kr/data/15068847/fileData.do
송파구(실외) (파일) https://www.data.go.kr/data/15090343/fileData.do
양천구 (API) https://www.data.go.kr/data/15040511/fileData.do#tab-layer-openapi
영등포구(실외) (API) https://www.data.go.kr/data/15069051/fileData.do#tab-layer-openapi

강북구 (API) https://www.data.go.kr/data/15049030/fileData.do#tab-layer-openapi
강서구 (API) https://www.data.go.kr/data/15037912/fileData.do#tab-layer-openapi
관악구 (파일) https://www.data.go.kr/data/15040591/fileData.do
광진구 (API) https://www.data.go.kr/data/15096573/openapi.do
금천구 (API) https://www.data.go.kr/data/15053428/fileData.do#tab-layer-openapi
노원구 (API) https://www.data.go.kr/data/15078097/fileData.do#tab-layer-openapi
동대문구 (API) https://www.data.go.kr/data/15070168/fileData.do#tab-layer-openapi
동작구 (API) https://www.data.go.kr/data/15049031/fileData.do#tab-layer-openapi
서대문구 (API) https://www.data.go.kr/data/15040413/fileData.do#tab-layer-openapi
서초구 (API) https://www.data.go.kr/data/15074379/fileData.do#tab-layer-openapi
성동구 (API) https://www.data.go.kr/data/15053434/fileData.do#tab-layer-openapi
성북구 (API) https://www.data.go.kr/data/15103853/fileData.do#tab-layer-openapi
용산구 (API) https://www.data.go.kr/data/15073796/fileData.do#tab-layer-openapi
종로구 (API) https://www.data.go.kr/data/15039323/fileData.do#tab-layer-openapi
중구 (API) https://www.data.go.kr/data/15080296/fileData.do#tab-layer-openapi
중랑구 (API) https://www.data.go.kr/data/15040636/fileData.do#tab-layer-openapi

   화장실:
   경기도 : https://www.data.go.kr/data/15011425/openapi.do
   대전 서구 : https://www.data.go.kr/data/15112469/openapi.do
   대전 유성구 : https://www.data.go.kr/data/15108840/openapi.do
   전북 전주 : https://www.data.go.kr/data/3036603/openapi.do
   제주 : https://www.data.go.kr/data/15109235/openapi.do
   부산 : https://www.data.go.kr/data/15070795/openapi.do

   전국 공중화장실 데이터 (엑셀파일) : https://www.data.go.kr/data/15012892/standard.do
