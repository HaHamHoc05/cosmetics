package cosmetic.usecase;

public interface OutputBoundary<Res extends ResponseData> {
	void present(Res response);
}
