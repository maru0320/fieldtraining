// src/component/Menu/menuList.js

const menu_list = [
    {
      menu_id: 1,
      title: "매칭",
      url: "/",
      subItems: [{ sub_id: 1, title: "매칭 현황", url: "/matching-status" }]
      
    },
    {
      menu_id: 2,
      title: "실습",
      url: "/practice",
      subItems: [
        { sub_id: 2, title: "실습 일지", url: "/practice-log" },
        { sub_id: 3, title: "수업 일지", url: "/class-log" },
        { sub_id: 4, title: "학급 운영 일지", url: "/class-management-log" },
        { sub_id: 5, title: "교직 실무 일지", url: "/teaching-practice-log" }
      ]
    },
    {
      menu_id: 3,
      title: "평가",
      url: "/evaluation",
      subItems: [
        { sub_id: 6, title: "평가", url: "/evaluation" },
        { sub_id: 7, title: "평가 결과 확인", url: "/evaluation-results" }
      ]
    },
    {
      menu_id: 4,
      title: "출결",
      url: "/attendance",
      subItems: [
        { sub_id: 8, title: "출석 현황", url: "/attendance-status" },
        { sub_id: 9, title: "결석계 제출", url: "/absence-report" }
      ]
    },
    {
      menu_id: 5,
      title: "일정",
      url: "/schedule",
      subItems: []
    }
  ];
  
  export default menu_list;
  