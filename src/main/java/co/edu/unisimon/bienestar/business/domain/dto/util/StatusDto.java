package co.edu.unisimon.bienestar.business.domain.dto.util;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@RequiredArgsConstructor(staticName = "create")
public class StatusDto implements Serializable {

    private static final long serialVersionUID = -7140637316858354020L;

    @NonNull
    private Boolean response;

}
