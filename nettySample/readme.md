netty sample

1. echo server/client 
	- [com.psc.netty.echo](src/com/psc/netty/echo) 참고
	- 에코 서버 클라이언 종료를 원할 경우 "END" 입력
	
	![screenshot](https://github.com/parkseungchul/javaSample/blob/master/nettySample/img/echoCient.png?raw=true) 
	
2. file 다운로드
	- process: client 다운로드 파일 선택 -> server 전송 -> client 파일 다운로드 완료   
	- [com.psc.netty.file](src/com/psc/netty/file) 참고
	- 중복 파일 업로드 경우 파일 삭제

3. file 큰  파일 다운로드
	- process: client 다운로드 파일 선택 -> server 전송 -> client 파일 다운로드 완료   
	- [com.psc.netty.file](src/com/psc/netty/file2) 참고
	- 중복 파일 업로드 삭제 로직 개선 	