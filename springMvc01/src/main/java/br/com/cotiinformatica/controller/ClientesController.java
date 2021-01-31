package br.com.cotiinformatica.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import br.com.cotiinformatica.entities.Cliente;
import br.com.cotiinformatica.repositories.interfaces.IClienteRepository;

@Controller
public class ClientesController {
	
	@Autowired
	private IClienteRepository clienteRepository;

	// m�todo para abrir a p�gina de cadastro de clientes
	@RequestMapping("/formularioCliente")
	public ModelAndView formularioCliente() {

		// definindo o caminho da p�gina que o m�todo ir� acessar atraves da ROTA..
		ModelAndView modelAndView = new ModelAndView("clientes/formulario-cliente");

		// enviando para a p�gina uma inst�ncia de cliente
		modelAndView.addObject("cliente", new Cliente());

		return modelAndView;
	}

	// m�todo para receber o SUBMIT POST do formul�rio
	@RequestMapping(value = "/cadastrarCliente", method = RequestMethod.POST)
	public ModelAndView cadastrarCliente(Cliente cliente, ModelMap map) {
				
		try {
			
			//verificando se o email informado j� encontra-se cadastrado..
			if(clienteRepository.findByEmail(cliente.getEmail()) != null) {
				throw new Exception("O email " + cliente.getEmail() + ", j� encontra-se cadastrado. Tente outro.");
			}
			
			//gravando o objeto cliente no banco de dados
			clienteRepository.create(cliente);
			
			// definindo uma mensagem para exibir na p�gina..
			map.addAttribute("mensagem_sucesso", "Cliente " + cliente.getNome() + ", cadastrado com sucesso.");
		}
		catch(Exception e) {
			map.addAttribute("mensagem_erro", e.getMessage());
		}	
		
		// criando um objeto ModelAndView
		ModelAndView modelAndView = new ModelAndView("clientes/formulario-cliente", map);
		// enviando para a p�gina uma inst�ncia de cliente
		modelAndView.addObject("cliente", new Cliente());
		return modelAndView;
	}

	// m�todo para abrir a p�gina de consulta de clientes
	@RequestMapping("/listagemClientes")
	public String listagemClientes() {
		// WEB-INF/views/clientes/listagem-clientes.jsp
		return "clientes/listagem-clientes";
	}
}