import com.kumuluz.ee.logs.cdi.Log;
import com.kumuluz.ee.logs.cdi.LogParams;
import com.rso.streaming.logic.ClipBean;
import io.swagger.annotations.Api;
import org.eclipse.microprofile.metrics.annotation.Metered;
import org.eclipse.microprofile.metrics.annotation.Timed;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.InputStream;

@Api(value = "Clips")
@RequestScoped
@Path("/clips")
@Log(LogParams.METRICS)
public class UserResource {

    @Inject
    private ClipBean clipBean;

    @Context
    protected UriInfo uriInfo;

    @GET
    @Path("/{clipId}")
    @Timed(name = "ClipsStreamGetTime")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getClip(@PathParam("clipId") long clipId) {
        InputStream file = clipBean.getFile(clipId);

        if(file == null){
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            return Response.ok(file, MediaType.APPLICATION_OCTET_STREAM)
                    //.header("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"" ) //optional
                    .build();
        }
    }

    @GET
    @Path("/healthResponseCheck")
    public Response getCheck() {
        return Response.status(Response.Status.OK).build();
    }

    @POST
    @Metered(name = "ClipFileCreation")
    @Path("/{clipId}")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    public Response createFile(InputStream input, @PathParam("clipId") long clipId) {
        if (clipBean.addFile(input, clipId)) {
            return Response.status(Response.Status.CREATED).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }


}
