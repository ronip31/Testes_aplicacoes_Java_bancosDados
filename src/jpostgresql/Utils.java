package jpostgresql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Scanner;

public class Utils {
	
	static Scanner teclado = new Scanner(System.in);
	
	public static Connection conectar () {
		//System.out.println("Conectando...");
		Properties props = new Properties();
		props.setProperty("user", "postgres");
		props.setProperty("password", "gesplan");
		props.setProperty("ssl", "false");
		String URL_SERVIDOR = "jdbc:postgresql://DESKTOP-TKCV6QN:5432/jpostgresql";
		
		try {
			return DriverManager.getConnection(URL_SERVIDOR, props);
		}catch(Exception e) {
			e.printStackTrace();
			if(e instanceof ClassNotFoundException) {
				System.err.println("Verifique o driver de conexão.");
				}else {
					System.err.println("Verifique se o servidor está ativo.");
				}
			System.exit(-42);
			return null;
		}
	}
	
	public static void desconectar(Connection conn) {
		//System.out.println("Desconectando...");
		if( conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void listar() {
		//System.out.println("Listando produtos...");
		String BUSCAR_TODOS = "select * from produtos";
		
		try {
			Connection conn = conectar();
			PreparedStatement produtos = conn.prepareStatement(
					BUSCAR_TODOS,
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
		
			ResultSet res = produtos.executeQuery();
			
			res.last();
			int qtd = res.getRow();
			res.beforeFirst();
			
			if(qtd > 0) {
				System.out.println("Listando produtos...");
				System.out.println("--------------------");
				System.out.println("id: " + res.getInt(1));
				System.out.println("Produto: " + res.getInt(2));
				System.out.println("Preco: " + res.getInt(3));
				System.out.println("Estoque: " + res.getInt(4));
				System.out.println("--------------------");
			}else {
				System.out.println("Nao existem produtos cadastrados.");
			}
		

		}catch(Exception e){
			e.printStackTrace();
			System.err.println("Erro buscando todos produtos.");
			System.exit(-42);
		}
	}
	public static void inserir() {
		System.out.println("Inserindo produtos...");
	}
	
	public static void atualizar() {
		System.out.println("Atualizando produtos...");
	}
	
	public static void deletar() {
		System.out.println("Deletando produtos...");
	}
	
	public static void menu() {
		System.out.println("==================Gerenciamento de Produtos===============");
		System.out.println("Selecione uma opção: ");
		System.out.println("1 - Listar produtos.");
		System.out.println("2 - Inserir produtos.");
		System.out.println("3 - Atualizar produtos.");
		System.out.println("4 - Deletar produtos.");
		
		int opcao = Integer.parseInt(teclado.nextLine());
		if(opcao == 1) {
			listar();
		}else if(opcao == 2) {
			inserir();
		}else if(opcao == 3) {
			atualizar();
		}else if(opcao == 4) {
			deletar();
		}else {
			System.out.println("Opção inválida.");
		}
	}
}
