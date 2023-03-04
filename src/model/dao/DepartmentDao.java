package model.dao;

import java.util.List;
import model.entities.Department;

//operações que serão feitas para departamento
public interface DepartmentDao {
	void insert(Department obj);
	void update(Department obj); 
	void deleteById(Integer id);
	Department findById(Integer id);
	List<Department> findAll();
	
	
}
