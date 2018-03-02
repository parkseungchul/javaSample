내가 어쩌다 이걸 만들려고 했는지...

서버와 클라언트 환경에서의  파일 업로드/다운로드 생각했는데 고려하고 생각 할 부분이 많다. 

예를 들어 업로드/다운로드 할 때 파일 이름 어떻게 넣을래? 추가 정보는 어디다 넣고 싶니? 전문을 어떻게 구성 할래? 최적의 속도가 나올까? 등등 

쉽게 가려고 netty 기반으로 시작했는데 네트워크 프로그래밍이 해 본적이 없어서 고생을 많이 했다.

... 이주일이 흐른 뒤

전문 방식으로 데이터를 주고 받을 수 있게 되었다.  

netty 구조를  살짝 이해하고  encoding/decoding만 신경써서 해주면 된다 (네티 우앙 ~ 굳)

추후 하고 싶은 건...

1. 속도 튜닝

2. 통신 모듈 아답터화 하여 Delimiter, XML, JSON 통신이 가능하게 ...

혼자 하긴 힘들고 같이 덕지덕지 붙여 갈 사람 있었음 좋겠다 ㅠ.ㅠ 

netty sample

1. echo server/client 
	- [com.psc.netty.echo](src/com/psc/netty/echo) 참고
	- 에코 서버 클라이언 종료를 원할 경우 "END" 입력
	
	![screenshot](https://github.com/parkseungchul/javaSample/blob/master/nettySample/img/echoClient.png?raw=true) 
	
2. file download basic
	- process: client 다운로드 파일 선택 -> file 전송 -> client 파일 다운로드 완료   
	- [com.psc.netty.file](src/com/psc/netty/file) 참고
	- 중복 파일 업로드 경우 파일 삭제

3. large file download
	- process: client 다운로드 파일 선택 -> file 전송 -> client 파일 다운로드 완료   
	- [com.psc.netty.file2](src/com/psc/netty/file2) 참고
	- 중복 파일 업로드 삭제 로직 개선 	
	
	![screenshot](https://github.com/parkseungchul/javaSample/blob/master/nettySample/img/fileClient1.png?raw=true) 
	
	![screenshot](https://github.com/parkseungchul/javaSample/blob/master/nettySample/img/fileClient2.png?raw=true)
	
	
4. file upload 
	- process: client 파일 선택 -> file 전송 -> server 업로드 완성
	- [com.psc.netty.file3](src/com/psc/netty/file2) 참고
	- 문제: 업로드 시 바이트를 잘라 파일명을 넣어야하는되 잘 되질 않아 일단 하드코딩	
	
5. file upload2
	- process: client 파일 선택 -> file 전송 -> server 업로드 완성
	- [com.psc.netty.file3](src/com/psc/netty/fixed) 참고
	- FixedLength 전문을 이용한 파일 업로드!! 
	- 문제: 속도 개선을 위한 튜닝은 하지 않음 ~ 	

	![screenshot](https://github.com/parkseungchul/javaSample/blob/master/nettySample/img/fixed1.png?raw=true)
	
6. broadcast와 udp 를 이용하여 파일 다운로드 프로그램 
	- process: client 기동 -> server로 부터 client에 파일 다운로드 
	- [com.psc.netty.udp](src/com/psc/netty/udp) 참고 - 네티 인 액션 책

	