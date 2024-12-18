import { useEffect, useState } from "react";
import axios from "axios";
import {jwtDecode} from 'jwt-decode';

const SchoolList = () => {
    const [teachers, setTeachers] = useState([]);
    const [students, setStudents] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [selectedTeacher, setSelectedTeacher] = useState(null); // 선택된 선생님 저장

    // 교사 리스트 가져오기
    useEffect(() => {
        const fetchTeachers = async () => {
            const token = localStorage.getItem("token");
            if (!token) {
                setError("로그인 상태가 아닙니다.");
                setLoading(false);
                return;
            }

            try {
                setLoading(true);

                // JWT 토큰에서 userId 추출
                const decodedToken = jwtDecode(token);
                const userId = decodedToken.userId;

                // API 호출
                const response = await axios.get(`http://localhost:8090/api/listmanagement/teachersByUserSchool/${userId}`);
                setTeachers(response.data); // 교사 데이터 업데이트
            } catch (err) {
                console.error("Error fetching teachers data", err);
                setError("선생님 정보를 불러오는 중 문제가 발생했습니다.");
            } finally {
                setLoading(false);
            }
        };

        fetchTeachers();
    }, []);

    // 선생님을 클릭했을 때 해당 선생님에 매칭된 학생들을 가져오는 함수
  const handleTeacherClick = async (teacherId) => {
    try {
      setLoading(true);
      const response = await axios.get(`http://localhost:8090/api/listmanagement/studentsForTeacher/${teacherId}`);
      setStudents(response.data);  // 학생 리스트 상태 업데이트
      setSelectedTeacher(teacherId);  // 선택된 선생님 ID 저장
    } catch (err) {
      console.error("Error fetching students data", err);
      setError("학생 정보를 불러오는 중 문제가 발생했습니다.");
    } finally {
      setLoading(false);
    }
  };

    return (
        <div>
            {/* 선생 리스트 */}
            <div>
                <p>선생님 목록</p>
                {loading && <p>로딩 중...</p>}
                {error && <p style={{ color: "red" }}>{error}</p>}
                <ul>
                    {teachers.length > 0 ? (
                        teachers.map((teacher) => (
                            <li key={teacher.id} onClick={() => handleTeacherClick(teacher.id)}>
                                <strong>{teacher.name}</strong><br />
                                이메일: {teacher.email}<br />
                                학교: {teacher.schoolName}<br />
                                과목: {teacher.subject}
                            </li>
                        ))
                    ) : (
                        !loading && <li>선생님 정보가 없습니다.</li>
                    )}
                </ul>
            </div>
            {/* 선택된 선생님에 매칭된 학생 리스트 */}
            {selectedTeacher && (
                <div>
                    <h3>{teachers.find(teacher => teacher.id === selectedTeacher)?.name}에 매칭된 학생들</h3>
                    <ul>
                        {students.length > 0 ? (
                            students.map((student) => (
                                <li key={student.id}>
                                    <strong>{student.name}</strong><br />
                                    학과: {student.department}<br />
                                    이메일: {student.email}
                                </li>
                            ))
                        ) : (
                            <li>매칭된 학생이 없습니다.</li>
                        )}
                    </ul>
                </div>
            )}
        </div>
    );
};

export default SchoolList;