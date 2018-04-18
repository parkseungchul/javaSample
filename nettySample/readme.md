서버 클라이언트로 TCP/IP 통신 요건이 발생하여 1번을 만들게 되었다.

(2 ~ 6번 프로그램을 만들면서 공부한 걸 바탕으로...)

기본 네티를 쓰게 된다면 보내는 대용량 데이터일 경우 채널이 inactive 되는 현상이 발생(내가 실수 한 것일 수도...)되어

FixedLength 방식으로 정해진 규격으로 데이터를 보내고 받아보고자 프로그램을 만들었다.

대용량 데이터일 경우도 자바 힙 메모리가 허용한 한 통신이 가능한 것으로 확인 되었으며

(대용량 파일을 보내고 받는 요건이 있다면 커스트 마이징을 통해 자바 힙 메모리 사이즈 무관하게 업로드/다운도로드 가능)

서버에 여러 클라이언트가 데이터를 요청해도 문제 없이 응답할 수 있도록 구현 되었다 

(client에서 uuid 생성하고 서버는 uuid를 이용하여 unique 하게 데이터를 저장)

ip, port, character set, size 등과 같이 서버에 따라 값이 달라질 수 있는 것들은 properties로 빼 놨다.

이 프로그램을 기반해서 운영 시스템에 리모트 로그 찾는 프로그램에 사용 되었다. 
  
개발자에게 TCP/IP 통신 프로그램 짜라고 하니 울고 있길래 

샘플 주고 시연해주니까 급 방긋하더랔 ㅋㅋㅋ 나는야 천사 갑~  

1. netty VO 통신  
	- 환경 점검 
		- config/env.properties 설정 확인
		- SERVER_SIZE: 서버에서 클라이언트로 보내는 VO byte 사이즈
		- CLIENT_SIZE: 클라이언트에서 서버로 보내는 VO byte 사이즈
		- SERVER_IP: 서버 IP
		- PORT: 서버 PORT
		- CHARACTER_SET: 언어셋	
	- 서버 기동: com.psc.netty.fixed2.server.FixedServer2 실행
		- com.psc.netty.fixed2.server.app.ServerApp2 에 서버에서 실행되는 업무 로직			
	- 클라이언트 기동: com.psc.netty.fixed2.client.FixedClient2 실행
		- com.psc.netty.fixed2.client.app.ClientApp2 에 클라이언트에서 실행되는 로직
				
	![screenshot](https://github.com/parkseungchul/javaSample/blob/master/nettySample/img/fixed2.png?raw=true) 

2. broadcast와 udp 를 이용하여 파일 다운로드 프로그램 
	- process: client 기동 -> server로 부터 client에 파일 다운로드 
	- [com.psc.netty.udp](src/com/psc/netty/udp) 참고 - 네티 인 액션 책
	
3. file upload2
	- process: client 파일 선택 -> file 전송 -> server 업로드 완성
	- [com.psc.netty.file3](src/com/psc/netty/fixed) 참고
	- FixedLength 전문을 이용한 파일 업로드!! 
	- 문제: 속도 개선을 위한 튜닝은 하지 않음 ~ 	

	![screenshot](https://github.com/parkseungchul/javaSample/blob/master/nettySample/img/fixed1.png?raw=true)
	
4. file upload 
	- process: client 파일 선택 -> file 전송 -> server 업로드 완성
	- [com.psc.netty.file3](src/com/psc/netty/file2) 참고
	- 문제: 업로드 시 바이트를 잘라 파일명을 넣어야하는되 잘 되질 않아 일단 하드코딩	

5. large file download
	- process: client 다운로드 파일 선택 -> file 전송 -> client 파일 다운로드 완료   
	- [com.psc.netty.file2](src/com/psc/netty/file2) 참고
	- 중복 파일 업로드 삭제 로직 개선 	
	
	![screenshot](https://github.com/parkseungchul/javaSample/blob/master/nettySample/img/fileClient1.png?raw=true) 
	
	![screenshot](https://github.com/parkseungchul/javaSample/blob/master/nettySample/img/fileClient2.png?raw=true)

6. file download basic
	- process: client 다운로드 파일 선택 -> file 전송 -> client 파일 다운로드 완료   
	- [com.psc.netty.file](src/com/psc/netty/file) 참고
	- 중복 파일 업로드 경우 파일 삭제
						
7. echo server/client 
	- [com.psc.netty.echo](src/com/psc/netty/echo) 참고
	- 에코 서버 클라이언 종료를 원할 경우 "END" 입력
	
	![screenshot](https://github.com/parkseungchul/javaSample/blob/master/nettySample/img/echoClient.png?raw=true) 
	


	

	

	



			
				




  
	