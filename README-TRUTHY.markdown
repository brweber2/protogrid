truthiness
    && ||                               # short circuits like you would expect
    if ( false ... )                    # false
    if ( nil ... )                      # false
    if ( anything-else ... )            # true
