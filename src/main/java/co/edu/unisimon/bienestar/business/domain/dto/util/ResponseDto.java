package co.edu.unisimon.bienestar.business.domain.dto.util;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor(staticName = "create")
public class ResponseDto<T> {

    @NonNull
    private T data;

}
