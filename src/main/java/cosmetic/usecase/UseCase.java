package cosmetic.usecase;

public interface UseCase<Req extends RequestData, Res extends ResponseData> extends InputBoundary<Req, Res> {

}