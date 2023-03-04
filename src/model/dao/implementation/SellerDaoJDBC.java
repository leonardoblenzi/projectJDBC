package model.dao.implementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao {
	
	private Connection conn;
	
	public SellerDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Seller obj) {
		PreparedStatement ps = null;
		
		try {
			ps = conn.prepareStatement("INSERT INTO seller"
					+ "(Name, Email, BirthDate, BaseSalary, DepartmentId) "
					+ "VALUES "
					+ "(?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			
			ps.setString(1, obj.getName());
			ps.setString(2, obj.getEmail());
			ps.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			ps.setDouble(4, obj.getBaseSalary());
			ps.setInt(5, obj.getDepartment().getId());
			
			int rowsAffected = ps.executeUpdate();
			
			if(rowsAffected > 0) {
				ResultSet rs = ps.getGeneratedKeys();
				if(rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
				DB.closeResultSet(rs);
			}
			else {
				throw new DbException("Unexpected error: No rows affected");
			}
			
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(ps);
		}
		
	}

	@Override
	public void update(Seller obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Seller findById(Integer id) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT seller.*,department.Name as DepName " 
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.Id "
					+ "WHERE seller.Id = ?");
			//setando o id
			ps.setInt(1, id);
			//armazendo valores no result set
			rs = ps.executeQuery();
			
			//verificando se foi encontrado o id
			if(rs.next()) {
				//chamando metodo que instancia departamento a partir do result set
				Department dep = instantiateDepartment(rs);
				
				//chamando metodo que instancia seller e passando dep e result set como parametro
				Seller obj = instantiateSeller(rs, dep);
				
				//retorna o objeto seller com associacao com dep
				return obj;
			}
			//se nao foi encontrado retorna nulo
			return null;
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(ps);
			DB.closeResultSet(rs);
		}
		
		
	}
	private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {
		Seller obj = new Seller();
		obj.setId(rs.getInt("Id"));
		obj.setName(rs.getString("Name"));
		obj.setEmail(rs.getString("Email"));
		obj.setBaseSalary(rs.getDouble("BaseSalary"));
		obj.setBirthDate(rs.getDate("BirthDate"));
		//associacao de seller com department, passando objeto inteiro
		obj.setDepartment(dep);
		
		return obj;
	}

	//metodo auxiliar para instanciar departamento
	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		Department dep = new Department();
		//pegando dados do departamento
		dep.setId(rs.getInt("DepartmentId"));
		dep.setName(rs.getString("DepName"));
		
		return dep;
	}

	@Override
	public List<Seller> findAll() {
			PreparedStatement ps = null;
			ResultSet rs = null;
			try {
				ps = conn.prepareStatement("SELECT seller.*,department.Name as DepName "
						+ "FROM seller INNER JOIN department "
						+ "ON seller.DepartmentId = department.Id "
						+ "ORDER BY Name");
				//setando o departamento
				
				//armazendo valores no result set
				rs = ps.executeQuery();
				
				//lista de seller para armazenar todos do mesmo dep
				List<Seller> list = new ArrayList<>();
				
				//tipo chave, valor
				Map<Integer, Department> map = new HashMap<>();
				
				//enquanto tiver valores
				while(rs.next()) {
					//verificando se o departamento ja existe no map
					Department dep = map.get(rs.getInt("DepartmentId"));
					
					//se dep nao existir vai criar, se não vai reaproveitar o existente
					if(dep == null) {
						//chamando metodo que instancia departamento a partir do result set
						dep = instantiateDepartment(rs);
						
						//id do dep, objeto dep
						map.put(rs.getInt("DepartmentId"), dep);
					}
					
					//chamando metodo que instancia seller e passando dep e result set como parametro
					Seller obj = instantiateSeller(rs, dep);
					
					//adiciona o objeto seller à lista
					list.add(obj);
				}
				//retornando a lista
				return list;
				
			}catch(SQLException e) {
				throw new DbException(e.getMessage());
			}
			finally {
				DB.closeStatement(ps);
				DB.closeResultSet(rs);
			}
	}

	@Override
	public List<Seller> findByDepartment(Department department) {
	PreparedStatement ps = null;
	ResultSet rs = null;
	try {
		ps = conn.prepareStatement("SELECT seller.*,department.Name as DepName "
				+ "FROM seller INNER JOIN department "
				+ "ON seller.DepartmentId = department.Id "
				+ "WHERE DepartmentId = ? "
				+ "ORDER BY Name");
		//setando o departamento
		ps.setInt(1, department.getId());
		
		//armazendo valores no result set
		rs = ps.executeQuery();
		
		//lista de seller para armazenar todos do mesmo dep
		List<Seller> list = new ArrayList<>();
		
		//tipo chave, valor
		Map<Integer, Department> map = new HashMap<>();
		
		//enquanto tiver valores
		while(rs.next()) {
			//verificando se o departamento ja existe no map
			Department dep = map.get(rs.getInt("DepartmentId"));
			
			//se dep nao existir vai criar, se não vai reaproveitar o existente
			if(dep == null) {
				//chamando metodo que instancia departamento a partir do result set
				dep = instantiateDepartment(rs);
				
				//id do dep, objeto dep
				map.put(rs.getInt("DepartmentId"), dep);
			}
			
			//chamando metodo que instancia seller e passando dep e result set como parametro
			Seller obj = instantiateSeller(rs, dep);
			
			//adiciona o objeto seller à lista
			list.add(obj);
		}
		//retornando a lista
		return list;
		
	}catch(SQLException e) {
		throw new DbException(e.getMessage());
	}
	finally {
		DB.closeStatement(ps);
		DB.closeResultSet(rs);
	}
		
	}
	
}
