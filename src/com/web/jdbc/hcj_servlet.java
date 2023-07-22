package com.web.jdbc;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import com.mysql.cj.x.protobuf.MysqlxDatatypes.Array;

/**
 * Servlet implementation class hcj_servlet
 */
@WebServlet("/hcj_servlet")
public class hcj_servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	
	private hcj_model hcj_model;
	
	//� ������ ���̽��� ������ ���ΰ�?
	@Resource(name="jdbc/project")
	private DataSource dataSource;


	@Override
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		hcj_model = new hcj_model(dataSource);
	}


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub	
		try {	
			String checkPage = request.getParameter("command");
	
			if (checkPage == null) {
				checkPage = "list";
			}
			
			switch(checkPage) {
				case "movieTime":
					movieTime(request, response);
					break;
				case "seatSelection":
					seatSelection(request, response);
					break;
				case "reservation":
					reservation(request, response);
					break;
				case "reservationticket":
					reservationTicket(request, response);
					break;
				case "cancel" :
					cancel(request, response);
					break;
				case "delete" :
					delete(request, response);
					break;
				default:
					listMovies(request, response);
			}
		}catch(Exception e) {
			log("error catch", e);
			throw new ServletException(e);
		}
	}
	
  





      
	   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	        
	         String CheckPage = request.getParameter("command");

	         switch(CheckPage){
	            case "checkId":
			         try {
			            checkId(request,response);
			         } catch (ServletException e1) {
			            // TODO Auto-generated catch block
			            e1.printStackTrace();
			         } catch (IOException e1) {
			            // TODO Auto-generated catch block
			            e1.printStackTrace();
			         } catch (SQLException e1) {
			            // TODO Auto-generated catch block
			            e1.printStackTrace();
			         }
			         break;
	            case "JOIN" :
	           
			         try {
			            joinMember(request,response);
			         } catch (ServletException | IOException | SQLException e) {
			            // TODO Auto-generated catch block
			            e.printStackTrace();
			         }
			         break;
	            case "nonUser" :
			         try {
			            nonUser(request,response);
			         } catch (ServletException | IOException | SQLException e) {
			            // TODO Auto-generated catch block
			            e.printStackTrace();
			         }
			         break;
	            case "Login":
			         try {
			            Login(request,response);
			         } catch (ServletException | IOException | SQLException e) {
			            // TODO Auto-generated catch block
			            e.printStackTrace();
			         } catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			         break;			               
	         }
	      }
	   //��ȸ�����Ÿ� ���� �޼ҵ�
	   private void nonUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
		   
		      String name = request.getParameter("name");
		      String birth = request.getParameter("birth");
		      String phone_num = request.getParameter("phone_num");

		}
   
	   //�α��� ������ ó���ϴ� �޼ҵ�
	      private void Login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
	            
	            String id = request.getParameter("id");
	            String pw = request.getParameter("password");
	           
	            if(hcj_model.Login(id, pw)==true) {
	            	//Model���� Login �޼ҵ忡�� HashMap ������� true�϶� id�� session���� ����
	               request.setAttribute("id",id);
	               request.setAttribute("password",pw);
	               HttpSession session = request.getSession();
	               session.setAttribute("id", id);
	                 
	               Integer sch_num = null;
	              if(session.getAttribute("sch_num") != null){   
	                  sch_num = (int)session.getAttribute("sch_num");   
	              }
	              
	               if(sch_num == null) {    
	            	   //sch_num session ���� ��������� main2 �������� �̵�������
	                  RequestDispatcher dispatcher = request.getRequestDispatcher("/main2.jsp");
	                   dispatcher.forward(request, response);
	               }else if(sch_num != null) {
	            	   //sch_num session���� ���� �Ҷ� session���� ������ reservation�� �����Ŵ
	                  String checkPage = "loginpage";
	                  session.setAttribute("checkPage", checkPage);
	                  response.sendRedirect(request.getContextPath()+"/hcj_servlet?command=reservation");
	               }
	       
	                 //���� �α��� �޼��尡 Ʈ�� �̸� Logingo.jsp���� ������ �۾��� ���� (������ /Logingo.jsp���� ����)
	            }else if (hcj_model.Login(id, pw)==false) {
	                RequestDispatcher dispatcher = request.getRequestDispatcher("/Login.jsp");     
	                    dispatcher.forward(request, response);
	            }
	            
	         }  
	    

   //ȸ�����Խ� ID �ߺ�üũ�� ���� �޼ҵ�
   public void checkId(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
	      String id = request.getParameter("id");

	      request.setAttribute("id", id);
	      request.setAttribute("check_id", hcj_model.checkId(id));

	     
	      if (hcj_model.checkId(id)==true) {
	    	  //id�� �����ϸ�  true���� ��ȯ�ϸ� CheckId2�� ������ �̵�
	         RequestDispatcher dispatcher = request.getRequestDispatcher("/CheckId_2.jsp");     
	         dispatcher.forward(request, response);
	         
	      }else if(hcj_model.checkId(id)==false){
	    	  //id �� �������� ������ insert���� id���� ������ Join_Member�� �̵�
	            request.setAttribute("id_", id);   
	            RequestDispatcher dispatcher = request.getRequestDispatcher("/Join_Member.jsp");     
	            dispatcher.forward(request, response);
	      }
	     
	}   
  

   //ȸ�������� ���� �޼ҵ�
   public void joinMember(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException{
       //����ǥ�������� input�� ����
       String reg = "^[a-zA-Z]*$";
       String regBirthdate = "^([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))$";
       String regPhoneNumber ="^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$";
       String id = request.getParameter("id_check");
       String password = request.getParameter("password");
       String name = request.getParameter("name");
       String birth = request.getParameter("birth");
       String phone_num = request.getParameter("phone_num");
       
       boolean a;
       boolean b;
       boolean c;
       boolean d;
         
       do{

	          b = Pattern.matches(regPhoneNumber, phone_num);
	          c = Pattern.matches(regBirthdate, birth);
	          d = Pattern.matches(reg, name);
         
	          if ( b==false || c ==false ||d == false) {
	        	  // �ϳ��� ������ ���Խ�ǥ���� ���� ������ validity_check�������� �̵�
	                RequestDispatcher dispatcher = request.getRequestDispatcher("/validity_check.jsp");     
	                dispatcher.forward(request, response);
	          }
         }while(!b && !c && !d);
       //input���� ����� �޾ƿ°�� ȸ�������� ȸ�����̺� �����ϱ� ����
                
         hcj_model.joinMember(id,name,birth,phone_num,password);
        
       
         RequestDispatcher dispatcher = request.getRequestDispatcher("/MovieList.jsp");     
         dispatcher.forward(request, response);
    }

   //���������� ������ҷ� ������Ʈ �����ֱ� ����
   private void cancel(HttpServletRequest request, HttpServletResponse response) throws Exception{
	      int index = Integer.parseInt(request.getParameter("tempindex"));
	      String multiple_seat = hcj_model.indexToMultipleseat(index);
	      hcj_model.cancel(index, multiple_seat);
	      
	      RequestDispatcher dispatcher = request.getRequestDispatcher("hcj_servlet?command=reservationticket");     
	      dispatcher.forward(request, response);
   }
   //���������� table���� �����ϱ� ����
	      private void delete(HttpServletRequest request, HttpServletResponse response) throws Exception{
	          int index = Integer.parseInt(request.getParameter("tempindex"));
	          String multiple_seat = hcj_model.indexToMultipleseat(index);
	          hcj_model.delete(index, multiple_seat);
	          
	          RequestDispatcher dispatcher = request.getRequestDispatcher("hcj_servlet?command=reservationticket");     
	          dispatcher.forward(request, response);
	       }
   
   //���������� Ȯ���ϱ� ���� �޼ҵ�
   private void reservationTicket(HttpServletRequest request, HttpServletResponse response) throws Exception{
	   //���ǿ��� (Stirng)id �� �ޱ�
	   HttpSession session = request.getSession();
	   String id = (String)session.getAttribute("id"); 

	   //id ��  (int)index�� ���� 
	   int user_id = hcj_model.sessionidChangeUserTable(id);


	  List<reservationTicket_dto> reservationTicket = hcj_model.reservationTicketLookUp(user_id);
	  request.setAttribute("reservationTicketTemp", reservationTicket);
	  
	  List<reservationTicket_dto> canceledTicket = hcj_model.canceledTicketLookUp(user_id);	  
	  request.setAttribute("canceledTicketTemp", canceledTicket);
	  //reservation ���̺� ������Ҹ� ������Ʈ �����ֱ� ����

	  RequestDispatcher dispatcher = request.getRequestDispatcher("/reservationCheck.jsp");	  
	  dispatcher.forward(request, response);	  

   }	
   
   @SuppressWarnings("unused")
private void reservation(HttpServletRequest request, HttpServletResponse response) throws Exception{
	      
	      //���ǿ��� (Stirng)id �� �ޱ�
	         HttpSession session = request.getSession();
	         String id = (String)session.getAttribute("id");

	      if(id == null) {
	    	  //session id�� ��������� 
	         int sch_num = Integer.parseInt(request.getParameter("sch_num"));
	         String[] seat_index = request.getParameterValues("seat_to_reserve");
	         session.setAttribute("sch_num", sch_num);
	         session.setAttribute("seat_index", seat_index);     
	         //seat_index�� ���������(�¼������� ����������) Checkseat���� ������ �̵�
	         if(seat_index==null){RequestDispatcher dispatcher = request.getRequestDispatcher("/Checkseat.jsp");
	            dispatcher.forward(request, response);}
	          //response.sendRedirect(request.getContextPath()+"/User_nonUser.jsp");
	         //seat_index�� ���� ������ User_nonUser�������� �̵�
	         else if(seat_index !=null) {RequestDispatcher dispatcher = request.getRequestDispatcher("/User_nonUser.jsp");
	            dispatcher.forward(request, response);//���� �޾ƿ� ���Ǿ��̵� null�̸� User_nonUser page�ΰ��� �α����̳� ��ȸ�� �α����� �ϵ��� ����
	         }
	      }else if(!id.equals("null")) {
	    	  //session id�� ������
	         String checkPage = (String)session.getAttribute("checkPage");
	         
	            //id ��  (int)index�� ���� 
	               int user_id = hcj_model.sessionidChangeUserTable(id);
	               // ���� ���̵� null�� �ƴ� ��쿡 reservation �޼��� ������ �����
	              
	              int sch_num = 0;
	              String[] seat_index = null;
	                 if(checkPage == null) {
	                    sch_num = Integer.parseInt(request.getParameter("sch_num"));
	                        seat_index = request.getParameterValues("seat_to_reserve");
	                 } else if(checkPage.equals("loginpage")) {
	                    sch_num = (int)session.getAttribute("sch_num");    
	                   seat_index = (String[])session.getAttribute("seat_index");  
	                 }
	                 //seat_index�� ��������� Checkseat �������� �̵�
	                 if(seat_index==null){RequestDispatcher dispatcher = request.getRequestDispatcher("/Checkseat.jsp");
			            dispatcher.forward(request, response);}
	                else if(seat_index != null) {
	               int check_user = 1;	//ckeck_user=1 > ȸ���ΰ��
	               
	               int nonuser_index = 0;	//check_user=0 > ��ȸ���ΰ��
	               int user_index = user_id; 
	               
	               List<reservation_dto> reservations = new ArrayList<>();
	               //�������� �¼��� ���õ� ��� List�� �޾ƿ��� ����
	                 for (int i = 0; i <seat_index.length; i++) {
	                     reservation_dto reservation = new reservation_dto(sch_num, Integer.parseInt(seat_index[i]), check_user, nonuser_index, user_index);
	                     if(check_user == 0) {
	                         hcj_model.nonuser_reservation(reservation);
	                      }else if(check_user == 1) {         
	                         hcj_model.user_reservation(reservation);
	                      }  
	                     reservations.add(reservation);                     
	                 }
	              
	                 session.removeAttribute("checkPage");
	                 session.removeAttribute("sch_num");
	                 session.removeAttribute("seat_index");
	              
	            // ������ �Ϸ�Ǹ� ����Ϸ� �������� �Ѱ���
	              List<reservationTicket_dto> reservationConfirm = hcj_model.reservations_complete(reservations);
	              request.setAttribute("reservationConfirm", reservationConfirm);
	              RequestDispatcher dispatcher = request.getRequestDispatcher("/reservationConfirm.jsp");
	              dispatcher.forward(request, response);   }         
	      } 
	   }   
   //��ȭ���� ����Ʈȭ �ؼ� �迭�� �ҷ����� ���� �޼ҵ�
	public void listMovies(HttpServletRequest request, HttpServletResponse response) throws Exception{

	    try {
	        List<MovieList_dto> Movies = hcj_model.getMovieList();
	        request.setAttribute("movie_list", Movies);
	     }catch(Exception e) {
	        e.printStackTrace();
	     }
	   
	      RequestDispatcher dispatcher = request.getRequestDispatcher("/MovieList.jsp");     
	      dispatcher.forward(request, response);
	   }
   
   
	

	//��ȭ �ð��� �ҷ����� ���� �޼ҵ�
	public void movieTime(HttpServletRequest request, HttpServletResponse response) throws Exception{
        
        // String movietitle = request.getParameter("moviename");
         //int movie_num = hcj_model.titleChangeMoiveNum(movietitle);         
         int movie_num = Integer.parseInt(request.getParameter("movieNum"));
         
         try {
              List<MovieList_dto> Movies = hcj_model.getMovieList();
              request.setAttribute("movie_info", Movies.get(movie_num-1));
           }catch(Exception e) {
              e.printStackTrace();
           }
         
         List<movie_dto> movieschedule = hcj_model.MovieTime(movie_num);
         request.setAttribute("scheduleList", movieschedule);
         
          RequestDispatcher dispatcher = request.getRequestDispatcher("/MovieTime.jsp");
            dispatcher.forward(request, response);
                 
      }      		   
	
	//�¼������� ���� �޼ҵ�
	private void seatSelection(HttpServletRequest request, HttpServletResponse response) throws Exception{
		//schedule���̺��� sch_num�� ������ �޾� �¼��� ��ȸ��
		int sch_num = Integer.parseInt(request.getParameter("sch_num"));
		request.setAttribute("sch_num", sch_num);
		//schedule�� �¼����� ������ȣ�� �ο��� �ش��ϴ� �������� �¼��� �ҷ���
		List<reservation_dto> seats = hcj_model.loadSeat(sch_num);
		request.setAttribute("select_seat", seats);	
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("/SeatSelection.jsp");	  
		dispatcher.forward(request, response);
	}
}