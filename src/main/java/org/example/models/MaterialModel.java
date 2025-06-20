package org.example.models;

import org.example.JDBCUtil;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MaterialModel {
    private int id;
    private String nombre;
    private String unidad_medida;
    private BigDecimal peso;
    boolean activo;

    public MaterialModel(int id, String nombre, String unidad_medida, BigDecimal peso, boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.unidad_medida = unidad_medida;
        this.peso = peso;
        this.activo = activo;
    }

    public MaterialModel(){}

    //getters

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getUnidad_medida() {
        return unidad_medida;
    }

    public BigDecimal getPeso() {
        return peso;
    }

    public boolean isActivo() {return activo;}

    //seters
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setUnidad_medida(String unidad_medida) {
        this.unidad_medida = unidad_medida;
    }

    public void setPeso(BigDecimal peso) {
        this.peso = peso;
    }
    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return nombre;
    }

    /// metodos para accesar a la base de datos
    private static final String SQL_INSERTAR = "INSERT INTO materiales (nombre, unidad_medida, peso)VALUES (?, ?, ?)";
    private static final String SQL_LISTAR_TODOS = "SELECT * FROM materiales WHERE activo=1";
    private static final String SQL_BUSCAR_POR_ID = "SELECT * FROM materiales WHERE id = ?";
    private static final String SQL_ELIMINAR_LOGICO = "UPDATE materiales SET activo = FALSE WHERE id = ?";
    private static final String SQL_ACTUALIZAR = " UPDATE materiales SET nombre = ?, unidad_medida = ?, peso = ?, activo = ?  WHERE id = ?";
    private static final String SQL_BUSCAR_LIKE = "SELECT * FROM materiales WHERE nombre LIKE ?";

    public List<MaterialModel> buscarPorNombreLike(String texto) throws Exception {
        List<MaterialModel> lista = new ArrayList<>();
        try (Connection con = JDBCUtil.getConection();
             PreparedStatement ps = con.prepareStatement(SQL_BUSCAR_LIKE)) {

            ps.setString(1, "%" + texto.trim() + "%"); // Agrega comodines para búsqueda parcial

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearMaterial(rs));
                }
            }
        }
        return lista;
    }

    public void actualizar(MaterialModel m) throws Exception {
        try (Connection con = JDBCUtil.getConection();
             PreparedStatement ps = con.prepareStatement(SQL_ACTUALIZAR)) {
            ps.setString(1, m.getNombre());
            ps.setString(2, m.getUnidad_medida());
            ps.setBigDecimal(3, m.getPeso());
            ps.setBoolean(4, m.isActivo()); // o getActivo() si es Boolean
            ps.setInt(5, m.getId());
            ps.executeUpdate();
        }
    }

    public void eliminarLogicamentePorId(int id) throws Exception {
        try (Connection con = JDBCUtil.getConection();
             PreparedStatement ps = con.prepareStatement(SQL_ELIMINAR_LOGICO)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
    public void guardar(MaterialModel m) throws Exception {
        try (Connection con = JDBCUtil.getConection();
             PreparedStatement ps = con.prepareStatement(SQL_INSERTAR)) {
            ps.setString(1, m.getNombre());
            ps.setString(2, m.getUnidad_medida());
            ps.setBigDecimal(3, m.getPeso());
            ps.executeUpdate();
        }
    }

    public List<MaterialModel> listarTodos() throws Exception {
        List<MaterialModel> lista = new ArrayList<>();
        try (Connection con = JDBCUtil.getConection();
             PreparedStatement ps = con.prepareStatement(SQL_LISTAR_TODOS);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapearMaterial(rs));
            }
        }
        return lista;
    }

    public MaterialModel buscarPorId(int id) throws Exception {
        try (Connection con = JDBCUtil.getConection();
             PreparedStatement ps = con.prepareStatement(SQL_BUSCAR_POR_ID)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearMaterial(rs);
                }
            }
        }
        return null;
    }

    private MaterialModel mapearMaterial(ResultSet rs) throws Exception {
        int id = rs.getInt("id");
        String nombre = rs.getString("nombre");
        String unidadMedida = rs.getString("unidad_medida");
        BigDecimal peso = rs.getBigDecimal("peso");
        boolean activo = rs.getBoolean("activo");
        return new MaterialModel(id, nombre, unidadMedida, peso,activo);
    }
}
