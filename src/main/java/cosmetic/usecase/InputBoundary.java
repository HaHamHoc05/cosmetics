package cosmetic.usecase;

public interface InputBoundary<Req extends RequestData, Res extends ResponseData> {
	void execute(Req request);

}
