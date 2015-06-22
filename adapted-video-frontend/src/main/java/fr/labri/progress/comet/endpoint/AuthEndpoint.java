package fr.labri.progress.comet.endpoint;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.ResponseBuilder;

import com.sun.research.ws.wadl.Response;

import fr.labri.progess.comet.model.FileType;
import fr.labri.progess.comet.model.FilterConfig;
import fr.labri.progess.comet.model.FilterConfigWrapper;
import fr.labri.progess.comet.model.HeaderFilter;

@Path("auth")
public class AuthEndpoint {

	@Path("github")
	@POST
	Response SignUpGitHub() {
		throw new WebApplicationException("505");
	}

}
