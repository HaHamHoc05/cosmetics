package cosmetic;

public interface UseCase<Req extends RequestData, Res extends ResponseData> 
extends InputBoundary<Req, Res> {
	
}